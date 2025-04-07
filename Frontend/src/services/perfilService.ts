import { db } from "../config/firebase";
import { setDoc, getDoc, doc } from "firebase/firestore";

export async function salvarPerfil(usuarioId: string, dados: object) {
  await setDoc(doc(db, "usuarios", usuarioId), dados);
}

export async function buscarPerfil(usuarioId: string) {
  const docSnap = await getDoc(doc(db, "usuarios", usuarioId));
  if (!docSnap.exists()) return null;
  return docSnap.data();
}

export async function atualizarHorariosAfericao(usuarioId: string, horarios: string[]) {
  await setDoc(doc(db, "usuarios", usuarioId), {
    horarios_afericao: horarios,
  }, { merge: true });
}
