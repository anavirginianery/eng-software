import Link from "next/link";
import Image from "next/image";

export default function Header() {
  return (
    <header className="w-full bg-white shadow p-4">
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <Link href="/">
          <Image
            src="/img/logo.png"
            alt="Logo"
            width={96}
            height={96}
            unoptimized
            className="h-8 w-auto"
          />{" "}
        </Link>
        <div>
          <Link href="/login" className="text-teal-600 mx-2 mr-4">
            Login
          </Link>
          <Link
            href="/cadastro"
            className="bg-teal-600 text-white px-4 py-2 rounded"
          >
            Cadastrar
          </Link>
        </div>
      </div>
    </header>
  );
}
