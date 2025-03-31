import { 
  createUserWithEmailAndPassword, 
  signInWithEmailAndPassword,
  updateProfile,
  onAuthStateChanged,
  User
} from "firebase/auth";
import { auth, db } from "../config/firebase";
import { doc, setDoc } from "firebase/firestore";

export const register = async (email: string, password: string, nome: string) => {
  try {
    const userCredential = await createUserWithEmailAndPassword(auth, email, password);
    const user = userCredential.user;

    // Atualiza o nome do usuário
    await updateProfile(user, { displayName: nome });

    // Cria o documento do usuário no Firestore
    await setDoc(doc(db, "usuarios", user.uid), {
      nome,
      email,
      dataCriacao: new Date()
    });

    return user;
  } catch (error: any) {
    console.error("Erro no registro:", error);
    throw error;
  }
};

export const login = async (email: string, password: string) => {
  try {
    const userCredential = await signInWithEmailAndPassword(auth, email, password);
    return userCredential.user;
  } catch (error: any) {
    console.error("Erro no login:", error);
    throw error;
  }
};

export const getCurrentUser = (): Promise<User | null> => {
  return new Promise((resolve, reject) => {
    const unsubscribe = onAuthStateChanged(auth,
      (user) => {
        unsubscribe();
        resolve(user);
      },
      (error) => {
        unsubscribe();
        reject(error);
      }
    );
  });
}; 