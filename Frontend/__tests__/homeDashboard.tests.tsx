import {
  limparColecao,
  salvarHorariosUsuario,
  criarMedicao,
  adicionarMedicao,
  buscarMedicoesDoUsuario,
  buscarMedicoesPorHorario,
  calcularMediaGlicemia,
  calcularMediaInsulina,
  buscarHorariosUsuario,
} from "../src/services/medicaoService";

import {
  initializeApp,
  deleteApp,
  getApp,
  getApps,
} from "firebase/app";

import {
  getFirestore,
  connectFirestoreEmulator,
} from "firebase/firestore";

// Inicialização isolada para ambiente de teste
const app =
  getApps().length === 0
    ? initializeApp({ projectId: "diabetter-dashboard-test" })
    : getApp();const db = getFirestore(app);
connectFirestoreEmulator(db, "localhost", 8080);

jest.setTimeout(20000);

const userId = "user-test-dashboard";

beforeEach(async () => {
  await limparColecao("medicoes");
  await limparColecao("usuarios");

  await salvarHorariosUsuario(userId, ["08:00", "12:00", "18:00"]);
});

describe("Dashboard (Firebase Emulator)", () => {
  it("D1: deve retornar medições do usuário", async () => {
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 110,
      horario: "08:00",
      nomeUsuario: "Moab",
    }));
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 120,
      horario: "12:00",
      nomeUsuario: "Moab",
    }));

    const snapshot = await buscarMedicoesDoUsuario(userId);
    expect(snapshot.empty).toBe(false);
    expect(snapshot.docs.length).toBe(2);
  });

  it("D2: deve filtrar medições por horário", async () => {
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 110,
      horario: "08:00",
      nomeUsuario: "Moab",
    }));
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 120,
      horario: "12:00",
      nomeUsuario: "Moab",
    }));

    const snapshot = await buscarMedicoesPorHorario(userId, "08:00");
    expect(snapshot.docs.length).toBe(1);
    expect(snapshot.docs[0].data().glicemia).toBe(110);
  });

  it("D3: deve calcular média de glicemia", async () => {
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 100,
      horario: "08:00",
      nomeUsuario: "Moab",
    }));
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 200,
      horario: "12:00",
      nomeUsuario: "Moab",
    }));

    const media = await calcularMediaGlicemia(userId);
    expect(media).toBe(150);
  });

  it("D4: deve calcular média de insulina", async () => {
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 100,
      horario: "08:00",
      nomeUsuario: "Moab",
      insulina: 5,
    }));
    await adicionarMedicao(criarMedicao({
      userId,
      glicemia: 200,
      horario: "12:00",
      nomeUsuario: "Moab",
      insulina: 15,
    }));

    const media = await calcularMediaInsulina(userId);
    expect(media).toBe(10);
  });

  it("D5: deve ler corretamente os horários de aferição do usuário", async () => {
    const horarios = await buscarHorariosUsuario(userId);
    expect(horarios).toContain("08:00");
    expect(horarios).toContain("12:00");
    expect(horarios).toContain("18:00");
  });
});

afterAll(async () => {
  await deleteApp(getApp());
});
