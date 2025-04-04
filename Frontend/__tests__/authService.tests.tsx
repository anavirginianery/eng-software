import { register, login, getCurrentUser } from '../src/services/authService';
import { db, auth } from '../src/config/firebase';
import { doc, getDoc } from 'firebase/firestore';
import { signOut } from 'firebase/auth';
import { deleteApp, getApp } from 'firebase/app';

describe('authService (Firebase Emulator)', () => {
  const email = `moab+${Date.now()}@test.com`;
  const senha = '123456';
  const nome = 'Moab Teste';

  it('R1 + R2: deve registrar um novo usuário e salvar no Firestore', async () => {
    const user = await register(email, senha, nome);
    expect(user.email).toBe(email);

    const snap = await getDoc(doc(db, 'usuarios', user.uid));
    expect(snap.exists()).toBe(true);
    expect(snap.data()?.nome).toBe(nome);
  });

  it('L1: deve logar com o usuário registrado', async () => {
    const user = await login(email, senha);
    expect(user.email).toBe(email);
  });

  it('L2: deve lançar erro ao logar com email inválido', async () => {
    await expect(login('naoexiste@test.com', senha))
    .rejects
    .toThrow('auth/user-not-found');
  });

  it('L3: deve lançar erro ao logar com senha incorreta', async () => {
    await expect(login(email, 'senhaErrada'))
      .rejects
      .toThrow('auth/wrong-password');
    });

  it('G1: deve retornar o usuário autenticado atual', async () => {
    await login(email, senha);
    const user = await getCurrentUser();
    expect(user?.email).toBe(email);
  });

  it('G2: deve retornar null se nenhum usuário estiver logado', async () => {
    await signOut(auth);
    const user = await getCurrentUser();
    expect(user).toBeNull();
  });

  it('R3: deve lançar erro ao tentar registrar com email inválido', async () => {
    const emailInvalido = 'isso-nao-é-email';
    const senha = '123456';
    const nome = 'Moab';

    await expect(register(emailInvalido, senha, nome))
      .rejects
      .toThrow('auth/invalid-email');
  });

  it('R4: deve lançar erro ao tentar registrar com senha fraca', async () => {
    const email = `moab-${Date.now()}@test.com`;
    const senhaFraca = '123';
    const nome = 'Moab';

    await expect(register(email, senhaFraca, nome))
      .rejects
      .toThrow('auth/weak-password');
  });
  
});

afterAll(async () => {
    const app = getApp();
    await deleteApp(app);
  });
  
