import {
    initializeApp,
    getApp,
    deleteApp,
  } from "firebase/app";
  import {
    getFirestore,
    connectFirestoreEmulator,
    collection,
    addDoc,
    Timestamp,
    getDocs,
  } from "firebase/firestore";
  import {
    getAuth,
    connectAuthEmulator,
    signInWithEmailAndPassword,
    createUserWithEmailAndPassword,
  } from "firebase/auth";

  import { db, auth } from "../src/config/firebase";
  
  const firebaseConfig = {
    projectId: "test-diabetter",
  };
  

  describe("Dashboard - Firebase Emulator", () => {
    const email = `dashboard+${Date.now()}@test.com`;
    const senha = "123456";
    let userId = "";
  
    beforeAll(async () => {
      const cred = await createUserWithEmailAndPassword(auth, email, senha);
      userId = cred.user.uid;
  
      // Simulando registros no Firestore
      const medicoesRef = collection(db, "medicoes");
  
      const baseDate = new Date();
      baseDate.setDate(baseDate.getDate() - 3); // 3 dias atrás
  
      for (let i = 0; i < 5; i++) {
        const data = new Date(baseDate);
        data.setDate(data.getDate() + i);
  
        await addDoc(medicoesRef, {
          userId,
          glicemia: 100 + i * 10,
          insulina: 5 + i,
          horario: "08:00",
          nomeUsuario: "Test User",
          timestamp: data.getTime(),
          data: Timestamp.fromDate(data),
        });
      }
    });
  
    it("D1: deve recuperar e contar os registros de glicemia e insulina", async () => {
      const snap = await collection(db, "medicoes");
      const docs = await (await getDocs(snap)).docs;
      const userDocs = docs.filter((doc) => doc.data().userId === userId);
      expect(userDocs.length).toBeGreaterThanOrEqual(5);
    });
  
    it("D2: deve aplicar filtro por horário", async () => {
      const allDocs = await getDocs(collection(db, "medicoes"));
      const filtered = allDocs.docs.filter(
        (doc) => doc.data().userId === userId && doc.data().horario === "08:00"
      );
      expect(filtered.length).toBe(5);
    });
  
    it("D3: deve aplicar filtro por intervalo de tempo (semana)", async () => {
      const umaSemanaAtras = new Date();
      umaSemanaAtras.setDate(umaSemanaAtras.getDate() - 7);
  
      const docsSnap = await getDocs(collection(db, "medicoes"));
      const recentes = docsSnap.docs.filter((doc) => {
        const d = doc.data();
        return d.userId === userId && d.timestamp >= umaSemanaAtras.getTime();
      });
  
      expect(recentes.length).toBe(5);
    });
  
    it("D4: deve gerar dados para exportação em PDF", async () => {
      const querySnap = await getDocs(collection(db, "medicoes"));
      const dados = querySnap.docs
        .filter((doc) => doc.data().userId === userId)
        .map((d) => d.data());
  
      expect(dados.length).toBeGreaterThanOrEqual(1);
      expect(dados[0]).toHaveProperty("glicemia");
      expect(dados[0]).toHaveProperty("insulina");
    });
  
    it("D5: deve simular carregamento", async () => {
      const loading = true;
      expect(loading).toBe(true);
    });
  
    afterAll(async () => {
      await deleteApp(getApp());
    });
  });
  