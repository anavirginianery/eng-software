import {
  limparColecao,
  criarMedicao,
  verificarDuplicidade,
  salvarMedicao,
  buscarMedicoesPorUsuarioHorario,
} from "../src/services/medicaoService";
import { getApp, deleteApp } from "firebase/app";

jest.setTimeout(10000);

const userId = "user-test-abc";
const horario = "08:00";

beforeEach(async () => {
  await limparColecao("medicoes");
});

describe("Medições (Firebase Emulator)", () => {
  it("M1: deve salvar uma medição válida e verificar no banco", async () => {
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

    const ref = await salvarMedicao(medicao);
    expect(typeof ref.id).toBe("string");

    const snapshot = await buscarMedicoesPorUsuarioHorario(userId, horario);
    const docs = snapshot.docs.map((doc) => doc.data());
    expect(docs.some((d) => d.glicemia === 120)).toBe(true);
  });

  it("M2: deve impedir medição duplicada no mesmo horário", async () => {
    const medicao = criarMedicao({
      userId,
      glicemia: 130,
      horario,
      nomeUsuario: "Moab",
    });

    // Simula já ter uma medição salva
    await salvarMedicao(medicao);

    const isDuplicado = await verificarDuplicidade(userId, horario);
    expect(isDuplicado).toBe(true);
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

    await expect(salvarMedicao(medicao)).rejects.toThrow(/unsupported field value/i);
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

    await expect(salvarMedicao(medicao)).rejects.toThrow(/unsupported field value/i);
  });
});

afterAll(async () => {
  await deleteApp(getApp());
});
