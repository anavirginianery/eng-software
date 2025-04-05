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
  } catch (error: unknown) {
    if (error instanceof Error) {
      if (error.message.includes('auth/invalid-credential')) {
        console.error("Email ou senha incorretos");
        throw new Error("Email ou senha incorretos");
      } else if (error.message.includes('auth/invalid-email')) {
        console.error("Email inválido");
        throw new Error("Email inválido");
      } else {
        console.error("Erro no login:", error.message);
        throw error;
      }
    } else {
      console.error("Erro desconhecido no login");
      throw new Error("Erro desconhecido no login");
    }
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