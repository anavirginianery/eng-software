import {
    initializeApp,
    getApp,
    deleteApp,
  } from "firebase/app";
  import {
    getFirestore,
    connectFirestoreEmulator,
    collection,
    setDoc,
    doc,
    addDoc,
    getDocs,
    query,
    where,
    deleteDoc,
  } from "firebase/firestore";
  import { db } from "../src/config/firebase";
  
  const firebaseConfig = { projectId: "diabetter-dashboard-test" };
  
  const userId = "user-dashboard-test";
  
  const limparColecao = async (colPath: string) => {
    const docsSnap = await getDocs(collection(db, colPath));
    const deletions = docsSnap.docs.map((doc) => deleteDoc(doc.ref));
    await Promise.all(deletions);
  };
  
  const criarMedicao = (glicemia: number, horario: string, insulina?: number) => {
    const now = new Date();
    const [h, m] = horario.split(":");
    now.setHours(Number(h), Number(m), 0, 0);
    return {
      userId,
      glicemia,
      horario,
      timestamp: now.getTime(),
      data: now,
      nomeUsuario: "Moab",
      ...(insulina && { insulina }),
    };
  };
  
  beforeEach(async () => {
    await limparColecao("medicoes");
    await setDoc(doc(db, "usuarios", userId), {
      horarios_afericao: ["08:00", "12:00", "18:00"]
    });
  });
  
  describe("Dashboard (Firebase Emulator)", () => {
    it("D1: deve retornar medições do usuário", async () => {
      await addDoc(collection(db, "medicoes"), criarMedicao(110, "08:00"));
      await addDoc(collection(db, "medicoes"), criarMedicao(120, "12:00"));
  
      const q = query(
        collection(db, "medicoes"),
        where("userId", "==", userId)
      );
      const snapshot = await getDocs(q);
      expect(snapshot.empty).toBe(false);
      expect(snapshot.docs.length).toBe(2);
    });
  
    it("D2: deve filtrar medições por horário", async () => {
      await addDoc(collection(db, "medicoes"), criarMedicao(110, "08:00"));
      await addDoc(collection(db, "medicoes"), criarMedicao(120, "12:00"));
  
      const q = query(
        collection(db, "medicoes"),
        where("userId", "==", userId),
        where("horario", "==", "08:00")
      );
      const snapshot = await getDocs(q);
      expect(snapshot.docs.length).toBe(1);
      expect(snapshot.docs[0].data().glicemia).toBe(110);
    });
  
    it("D3: deve calcular média de glicemia", async () => {
      await addDoc(collection(db, "medicoes"), criarMedicao(100, "08:00"));
      await addDoc(collection(db, "medicoes"), criarMedicao(200, "12:00"));
  
      const q = query(collection(db, "medicoes"), where("userId", "==", userId));
      const snapshot = await getDocs(q);
      const media = snapshot.docs.reduce((acc, doc) => acc + doc.data().glicemia, 0) / snapshot.docs.length;
      expect(media).toBe(150);
    });
  
    it("D4: deve calcular média de insulina", async () => {
      await addDoc(collection(db, "medicoes"), criarMedicao(100, "08:00", 5));
      await addDoc(collection(db, "medicoes"), criarMedicao(200, "12:00", 15));
  
      const q = query(collection(db, "medicoes"), where("userId", "==", userId));
      const snapshot = await getDocs(q);
      const docs = snapshot.docs.map((doc) => doc.data()).filter((d) => d.insulina !== undefined);
      const media = docs.reduce((acc, curr) => acc + curr.insulina, 0) / docs.length;
  
      expect(media).toBe(10);
    });
  
    it("D5: deve ler corretamente os horários de aferição do usuário", async () => {
      const userDoc = await getDocs(
        query(collection(db, "usuarios"), where("__name__", "==", userId))
      );
      const horarios = userDoc.docs[0].data().horarios_afericao;
      expect(horarios).toContain("08:00");
      expect(horarios).toContain("12:00");
      expect(horarios).toContain("18:00");
    });
  });
  
  afterAll(async () => {
    await deleteApp(getApp());
  });
  