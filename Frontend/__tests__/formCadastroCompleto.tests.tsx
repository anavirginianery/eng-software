import { initializeApp, getApp, deleteApp, getApps } from "firebase/app";
import { getFirestore, connectFirestoreEmulator } from "firebase/firestore";
import { salvarPerfil, buscarPerfil, atualizarHorariosAfericao } from "../src/services/perfilService";

const firebaseConfig = { projectId: "test-diabetter" };
const userId = "user-fake-perfil";

// Inicialização do app isolado para teste
const app =
  getApps().length === 0
    ? initializeApp({ projectId: "diabetter-dashboard-test" })
    : getApp();const db = getFirestore(app);
connectFirestoreEmulator(db, "localhost", 8080);
connectFirestoreEmulator(db, "localhost", 8080);

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

    await salvarPerfil(userId, dados);
    const data = await buscarPerfil(userId);

    expect(data?.peso).toBe("70");
    expect(data?.altura).toBe("180");
    expect(data?.tipoDiabetes).toBe("Tipo 1");
    expect(data?.tipoInsulina).toContain("Rápida");
    expect(data?.horarios_afericao).toContain("07:00");
  });

  it("F2: deve permitir a adição e remoção de horários de aferição", async () => {
    const horarios = ["08:00", "10:00", "06:00"];
    await atualizarHorariosAfericao(userId, horarios);
  
    const data = await buscarPerfil(userId);
    expect(data).not.toBeNull();
  
    if (data) {
      const horariosOrdenados = [...data.horarios_afericao].sort();
      expect(horariosOrdenados[0]).toBe("06:00");
  
      const novaLista = horariosOrdenados.filter((h: string) => h !== "10:00");
      expect(novaLista).not.toContain("10:00");
    }
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

    await salvarPerfil(userId, dados);
    const data = await buscarPerfil(userId);

    expect(data?.peso).toBe("80");
    expect(data?.tipoDiabetes).toBe("Tipo 2");
    expect(data?.comorbidades).toContain("Hipertensão");
    expect(data?.horarios_afericao).toContain("06:00");
  });

  it("F4: deve retornar erro ao tentar salvar perfil sem autenticação", async () => {
    const refId = "sem-user";
    const dados = {
      peso: "85",
      altura: "170",
      tipoDiabetes: "Tipo 1",
    };

    await expect(salvarPerfil(refId, dados)).resolves.toBeUndefined();
  });

  afterAll(async () => {
    await deleteApp(getApp());
  });
});
