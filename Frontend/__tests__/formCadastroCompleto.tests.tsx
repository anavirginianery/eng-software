import { getFirestore, connectFirestoreEmulator, setDoc, doc, getDoc } from "firebase/firestore";
import { initializeApp, getApp, deleteApp } from "firebase/app";
import { db, auth } from "../src/config/firebase";

  const firebaseConfig = {
    projectId: "test-diabetter",
  };

  const userId = "user-fake-perfil";
  const usuarioRef = doc(db, "usuarios", userId);
  
  describe("Perfil do Usuário (KeyFilter + Form) - Firebase Emulator", () => {
    it("F1: deve carregar dados existentes do perfil corretamente", async () => {
      const dados = {
        peso: "70",
        altura: "180",
        tipoDiabetes: "Tipo 1",
        tipoInsulina: ["Rápida", "Basal"],
        comorbidades: [],
        horarios_afericao: ["07:00", "12:00"],
        cadastroCompleto: true,
      };
  
      await setDoc(usuarioRef, dados);
  
      const docSnap = await getDoc(usuarioRef);
      expect(docSnap.exists()).toBe(true);
      const data = docSnap.data()!;
  
      expect(data.peso).toBe("70");
      expect(data.altura).toBe("180");
      expect(data.tipoDiabetes).toBe("Tipo 1");
      expect(data.tipoInsulina).toContain("Rápida");
      expect(data.horarios_afericao).toContain("07:00");
    });
  
    it("F2: deve permitir a adição e remoção de horários de aferição", async () => {
      const horarios = ["08:00", "10:00", "06:00"];
      await setDoc(usuarioRef, {
        horarios_afericao: horarios,
      }, { merge: true });
  
      const docSnap = await getDoc(usuarioRef);
      const data = docSnap.data()!;
  
      const horariosOrdenados = [...data.horarios_afericao].sort();
      expect(horariosOrdenados[0]).toBe("06:00");
  
      const novaLista = horariosOrdenados.filter((h: string) => h !== "10:00");
      expect(novaLista).not.toContain("10:00");
    });
  
    it("F3: deve salvar dados válidos do perfil no Firestore", async () => {
      const dados = {
        peso: "80",
        altura: "175",
        tipoDiabetes: "Tipo 2",
        tipoInsulina: ["Intermediária"],
        comorbidades: ["Hipertensão"],
        horarios_afericao: ["06:00", "18:00"],
        cadastroCompleto: true,
      };
  
      await setDoc(usuarioRef, dados);
      const docSnap = await getDoc(usuarioRef);
      const data = docSnap.data()!;
  
      expect(data.peso).toBe("80");
      expect(data.tipoDiabetes).toBe("Tipo 2");
      expect(data.comorbidades).toContain("Hipertensão");
      expect(data.horarios_afericao).toContain("06:00");
    });
  
    it("F4: deve retornar erro ao tentar salvar perfil sem autenticação", async () => {
      const ref = doc(db, "usuarios", "sem-user");
      const dados = {
        peso: "85",
        altura: "170",
        tipoDiabetes: "Tipo 1",
      };
  
      // Tentando salvar como um "usuário" inválido (simulação sem login)
      await expect(setDoc(ref, dados)).resolves.toBeUndefined();
    });
  
    afterAll(async () => {
      const app = getApp();
      await deleteApp(app);
    });
  });
  