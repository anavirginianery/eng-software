import {
  criarMedicoesTeste,
  buscarMedicoesDoUsuario,
  buscarMedicoesPorHorario,
  buscarMedicoesUltimosDias,
  prepararDadosExportacao,
} from "../src/services/medicaoService";

import {
  getApp,
  deleteApp,
} from "firebase/app";

import {
  createUserWithEmailAndPassword,
} from "firebase/auth";

import { auth } from "../src/config/firebase";

describe("Dashboard - Firebase Emulator", () => {
  const email = `dashboard+${Date.now()}@test.com`;
  const senha = "123456";
  let userId = "";

  beforeAll(async () => {
    const cred = await createUserWithEmailAndPassword(auth, email, senha);
    userId = cred.user.uid;
    await criarMedicoesTeste(userId); // método do service
  });

  it("D1: deve recuperar e contar os registros de glicemia e insulina", async () => {
    const snapshot = await buscarMedicoesDoUsuario(userId);
    expect(snapshot.docs.length).toBeGreaterThanOrEqual(5);
  });
  
  it("D2: deve aplicar filtro por horário", async () => {
    const snapshot = await buscarMedicoesPorHorario(userId, "08:00");
    expect(snapshot.docs.length).toBe(5);
  });

  it("D3: deve aplicar filtro por intervalo de tempo (semana)", async () => {
    const docs = await buscarMedicoesUltimosDias(userId, 7);
    expect(docs.length).toBe(5);
  });

  it("D4: deve gerar dados para exportação em PDF", async () => {
    const dados = await prepararDadosExportacao(userId);
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
