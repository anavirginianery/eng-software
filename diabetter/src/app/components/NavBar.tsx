import Link from "next/link";

export default function Navbar() {
  return (
    <nav className="p-4 bg-gray-100 shadow-md">
      <ul className="flex space-x-4">
        <li>
          <Link href="/">Home</Link>
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
  );
}
