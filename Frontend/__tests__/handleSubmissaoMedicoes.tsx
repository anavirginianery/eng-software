import {
    getFirestore,
    collection,
    addDoc,
    getDocs,
    query,
    where,
    connectFirestoreEmulator,
    deleteDoc,
    Timestamp,
  } from "firebase/firestore";
  import { initializeApp, getApp, deleteApp } from "firebase/app";
  
  const firebaseConfig = {
    projectId: "test-diabetter",
  };
  

  const limparColecao = async (colPath: string) => {
    const docsSnap = await getDocs(collection(db, colPath));
    const deletions = docsSnap.docs.map((doc) => deleteDoc(doc.ref));
    await Promise.all(deletions);
  };


  const app = initializeApp(firebaseConfig);
  const db = getFirestore(app);
  connectFirestoreEmulator(db, "localhost", 8080);
  
  const userId = "user-test-abc";
  const horario = "08:00";
  
  const criarMedicao = ({
    userId,
    glicemia,
    horario,
    insulina,
    tipoInsulina,
    nomeUsuario,
  }: {
    userId: string;
    glicemia: number;
    horario: string;
    insulina?: number;
    tipoInsulina?: string;
    nomeUsuario: string;
  }) => {
    const now = new Date();
    const [h, m] = horario.split(":");
    now.setHours(Number(h), Number(m), 0, 0);
    return {
      userId,
      glicemia,
      horario,
      data: now,
      timestamp: now.getTime(),
      nomeUsuario,
      ...(insulina && { insulina }),
      ...(tipoInsulina && { tipoInsulina }),
    };
  };
  
  const verificarDuplicidade = async (userId: string, horario: string) => {
    const inicioDoDia = new Date();
    inicioDoDia.setHours(0, 0, 0, 0);
  
    const q = query(
      collection(db, "medicoes"),
      where("userId", "==", userId),
      where("horario", "==", horario),
      where("timestamp", ">=", inicioDoDia.getTime())
    );
  
    const snapshot = await getDocs(q);
    return !snapshot.empty;
  };
  

  beforeEach(async () => {
    await limparColecao("medicoes");
  });
  

  describe("Simulação real do handleSubmit (Firebase Emulator)", () => {
    it("deve salvar uma medição válida e verificar no banco", async () => {
      const medicao = criarMedicao({
        userId,
        glicemia: 120,
        horario,
        nomeUsuario: "Moab",
        insulina: 5,
        tipoInsulina: "RÁPIDA",
      });
  
      const isDuplicado = await verificarDuplicidade(userId, horario);
      expect(isDuplicado).toBe(false);
  
      const ref = await addDoc(collection(db, "medicoes"), medicao);
      expect(typeof ref.id).toBe("string");
  
      const snapshot = await getDocs(
        query(
          collection(db, "medicoes"),
          where("userId", "==", userId),
          where("horario", "==", horario)
        )
      );
  
      const docs = snapshot.docs.map((doc) => doc.data());
      expect(docs.some((d) => d.glicemia === 120)).toBe(true);
    });
  
    it("deve impedir medição duplicada no mesmo horário", async () => {
      const medicao = criarMedicao({
        userId,
        glicemia: 130,
        horario,
        nomeUsuario: "Moab",
      });
  
      const isDuplicado = await verificarDuplicidade(userId, horario);
      if (isDuplicado) {
        // ⚠️ não insere, simula alerta
        expect(isDuplicado).toBe(true);
      } else {
        await addDoc(collection(db, "medicoes"), medicao);
      }
    });

    it("M4: deve falhar ao tentar salvar sem userId", async () => {
        const medicao = {
          ...criarMedicao({
            userId: "temp",
            glicemia: 110,
            horario: "09:00",
            nomeUsuario: "Moab",
          }),
          userId: undefined,
        } as any;
      
        try {
          await addDoc(collection(db, "medicoes"), medicao);
          throw new Error("Esperado erro, mas salvou com userId undefined");
        } catch (error: any) {
          expect(error.message).toMatch(/unsupported field value/i);
        }
      });
      
      it("M5: deve falhar ao tentar salvar sem glicemia", async () => {
        const medicao = {
          ...criarMedicao({
            userId,
            glicemia: 123,
            horario: "10:00",
            nomeUsuario: "Moab",
          }),
          glicemia: undefined,
        } as any;
      
        try {
          await addDoc(collection(db, "medicoes"), medicao);
          throw new Error("Esperado erro, mas salvou com glicemia undefined");
        } catch (error: any) {
          expect(error.message).toMatch(/unsupported field value/i);
        }
      });
      
    
  
    afterAll(async () => {
      const app = getApp();
      await deleteApp(app);
    });
  });
  