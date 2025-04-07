import {
    collection,
    getDocs,
    query,
    where,
    setDoc,
    doc,
    deleteDoc,
    addDoc,
    Timestamp
  } from "firebase/firestore";
  import { db } from "../config/firebase";
  
  /**
   * Remove todos os documentos de uma coleção.
   */
  export async function limparColecao(colPath: string) {
    const docsSnap = await getDocs(collection(db, colPath));
    const deletions = docsSnap.docs.map((doc) => deleteDoc(doc.ref));
    await Promise.all(deletions);
  }
  
  /**
   * Cria um objeto de medição com os campos padrão.
   */
  export function criarMedicao({
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
  }) {
    const now = new Date();
    const [h, m] = horario.split(":");
    now.setHours(Number(h), Number(m), 0, 0);
  
    return {
      userId,
      glicemia,
      horario,
      timestamp: now.getTime(),
      data: now,
      nomeUsuario,
      ...(insulina !== undefined && { insulina }),
      ...(tipoInsulina !== undefined && { tipoInsulina }),
    };
  }
  
  /**
   * Cria múltiplas medições para teste.
   */
  export const criarMedicoesTeste = async (userId: string) => {
    const medicoesRef = collection(db, "medicoes");
    const baseDate = new Date();
    baseDate.setDate(baseDate.getDate() - 3);
  
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
  };
  
  /**
   * Salva os horários de aferição de um usuário.
   */
  export async function salvarHorariosUsuario(userId: string, horarios: string[]) {
    await setDoc(doc(db, "usuarios", userId), {
      horarios_afericao: horarios,
    });
  }
  
  /**
   * Retorna todas as medições de um usuário.
   */
  export async function buscarMedicoesDoUsuario(userId: string) {
    const q = query(collection(db, "medicoes"), where("userId", "==", userId));
    return await getDocs(q);
  }
  
  /**
   * Retorna as medições de um usuário em um horário específico.
   */
  export async function buscarMedicoesPorHorario(userId: string, horario: string) {
    const q = query(
      collection(db, "medicoes"),
      where("userId", "==", userId),
      where("horario", "==", horario)
    );
    return await getDocs(q);
  }
  
  /**
   * Calcula a média de glicemia para um usuário.
   */
  export async function calcularMediaGlicemia(userId: string) {
    const snapshot = await buscarMedicoesDoUsuario(userId);
    const soma = snapshot.docs.reduce((acc, doc) => acc + doc.data().glicemia, 0);
    return snapshot.docs.length ? soma / snapshot.docs.length : 0;
  }
  
  /**
   * Calcula a média de insulina para um usuário.
   */
  export async function calcularMediaInsulina(userId: string) {
    const snapshot = await buscarMedicoesDoUsuario(userId);
    const docs = snapshot.docs
      .map((doc) => doc.data())
      .filter((d) => d.insulina !== undefined);
    const soma = docs.reduce((acc, curr) => acc + curr.insulina, 0);
    return docs.length ? soma / docs.length : 0;
  }
  
  /**
   * Retorna os horários de aferição cadastrados de um usuário.
   */
  export async function buscarHorariosUsuario(userId: string): Promise<string[]> {
    const userDoc = await getDocs(
      query(collection(db, "usuarios"), where("__name__", "==", userId))
    );
    if (!userDoc.empty) {
      return userDoc.docs[0].data().horarios_afericao || [];
    }
    return [];
  }
  
  /**
   * Adiciona uma nova medição no Firestore.
   */
  export async function adicionarMedicao(medicao: any) {
    await addDoc(collection(db, "medicoes"), medicao);
  }
  
  /**
   * Verifica se há uma medição duplicada no mesmo dia e horário.
   */
  export const verificarDuplicidade = async (userId: string, horario: string) => {
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
  
  /**
   * Salva uma medição (helper direto).
   */
  export const salvarMedicao = async (medicao: any) => {
    return await addDoc(collection(db, "medicoes"), medicao);
  };
  
  /**
   * Busca medições filtrando por userId e horário.
   */
  export const buscarMedicoesPorUsuarioHorario = async (userId: string, horario: string) => {
    const q = query(
      collection(db, "medicoes"),
      where("userId", "==", userId),
      where("horario", "==", horario)
    );
    return await getDocs(q);
  };
  
  /**
   * Busca medições recentes dentro de X dias.
   */
  export const buscarMedicoesUltimosDias = async (userId: string, dias: number) => {
    const limite = new Date();
    limite.setDate(limite.getDate() - dias);
  
    const snap = await getDocs(collection(db, "medicoes"));
    return snap.docs.filter(
      (doc) => doc.data().userId === userId && doc.data().timestamp >= limite.getTime()
    );
  };
  
  /**
   * Prepara os dados do usuário para exportação em PDF.
   */
  export const prepararDadosExportacao = async (userId: string) => {
    const snap = await getDocs(collection(db, "medicoes"));
    return snap.docs
      .filter((doc) => doc.data().userId === userId)
      .map((doc) => doc.data());
  };