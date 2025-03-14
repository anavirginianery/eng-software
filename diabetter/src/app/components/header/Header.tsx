import Link from "next/link";

export default function Header() {
  return (
    <header className="w-full bg-white shadow p-4">
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <div className="text-2xl font-bold text-teal-600">diabetter</div>
        <nav>
          <ul className="flex space-x-4">
            <li>
              <Link href="/pages/home">Home</Link>
            </li>
            <li>
              <Link href="/pages/dashboard">Dashboard</Link>
            </li>
            <li>
              <Link href="/pages/form">Formulário</Link>
            </li>
            <li>
              <Link href="/pages/login">Login</Link>
            </li>
            <li>
              <Link href="/pages/logout">Sair</Link>
            </li>
          </ul>
        </nav>
        <div>
          <Link href="/login" className="text-teal-600 mx-2">Login</Link>
          <Link href="/register" className="bg-teal-600 text-white px-4 py-2 rounded">Cadastrar</Link>
        </div>
      </div>
    </header>
  );
}