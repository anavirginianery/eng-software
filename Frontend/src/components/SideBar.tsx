"use client";

import React, { useState } from "react";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import Image from "next/image";
import {
  Users,
  ChartBar,
  FileQuestion,
  TrendingUp,
  LogOut,
  X,
  Menu,
} from "lucide-react";
import { getAuth, signOut } from "firebase/auth";

interface SidebarProps {
  className?: string;
}

const UserSidebar: React.FC<SidebarProps> = ({ className = "" }) => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const pathname = usePathname();
  const router = useRouter();

  const isActive = (path: string) => pathname.startsWith(path);
  const toggleMobileMenu = () => setIsMobileMenuOpen(!isMobileMenuOpen);

  const handleLogout = async () => {
    try {
      const auth = getAuth();
      await signOut(auth);
      router.push("/");
    } catch (error) {
      console.error("Erro ao fazer logout:", error);
    }
  };

  const MobileHeader = () => (
    <header className="fixed top-0 left-0 right-0 z-20 flex items-center justify-between h-16 px-4 bg-[#FFFFFF] border-b border-gray-300 lg:hidden">
      <Link href="/home">
        <Image
          src="/img/logo.png"
          alt="Logo"
          width={64}
          height={64}
          unoptimized
          className="h-8 w-auto"
        />
      </Link>

      {!isMobileMenuOpen && (
        <button
          onClick={toggleMobileMenu}
          className="p-2 text-gray-400 hover:text-gray-200"
        >
          <Menu className="w-6 h-6" />
        </button>
      )}
    </header>
  );

  const MobileMenu = () =>
    isMobileMenuOpen && (
      <div className="lg:hidden">
        <div
          className="fixed inset-0 bg-black/50 z-30"
          onClick={toggleMobileMenu}
        />
        <div className="fixed top-0 right-0 bottom-0 w-64 bg-[#FFFFFF] z-40 flex flex-col">
          <div className="h-16 px-4 flex items-center justify-end border-b border-gray-300">
            <button
              onClick={toggleMobileMenu}
              className="p-2 text-gray-400 hover:text-gray-200"
            >
              <X className="w-6 h-6" />
            </button>
          </div>
          <nav className="flex-1 p-4 space-y-1">
            <Link
              href="/home"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <Users className="w-4 h-4" />
              <span>Home</span>
            </Link>

            <Link
              href="/dashboard"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <ChartBar className="w-4 h-4" />
              <span>Dashboard</span>
            </Link>

            <Link
              href="/form"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <FileQuestion className="w-4 h-4" />
              <span>Formulário</span>
            </Link>

            <Link
              href="/inserirDados"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <TrendingUp className="w-4 h-4" />
              <span>Medições</span>
            </Link>
          </nav>
          <div className="mt-auto flex justify-center items-center">
            <button
              className="flex items-center justify-center gap-2 mb-2 w-32 rounded-lg p-2.5 text-sm hover:bg-teal-800/80 text-center"
              onClick={handleLogout}
            >
              <LogOut className="w-4 h-4" />
              Sair
            </button>
          </div>
        </div>
      </div>
    );

  const DesktopSidebar = () => (
    <aside
      className={`fixed top-0 left-0 bottom-0 w-64 bg-[#FFFFFF] border-r border-gray-300 hidden lg:block ${className}`}
    >
      <div className="h-16 px-4 flex items-center">
        <Image
          src="/img/logo.png"
          alt="Logo"
          width={64}
          height={64}
          unoptimized
          className="h-8 w-auto"
        />
      </div>
      <div className="flex flex-col h-[calc(100%-4rem)]">
        <nav className="p-3 space-y-1">
          <Link
            href="/home"
            className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/home")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
          >
            <Users className="w-4 h-4" />
            <span>Home</span>
          </Link>

          <Link
            href="/dashboard"
            className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/dashboard")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
          >
            <ChartBar className="w-4 h-4" />
            <span>Dashboard</span>
          </Link>

          <Link
            href="/form"
            className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/form")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
          >
            <FileQuestion className="w-4 h-4" />
            <span>Formulário</span>
          </Link>

          <Link
            href="/inserirDados"
            className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/inserirDados")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
          >
            <TrendingUp className="w-4 h-4" />
            <span>Medições</span>
          </Link>
        </nav>
        <div className="mt-auto flex justify-center items-center">
          <button
            onClick={handleLogout}
            className="flex items-center justify-center gap-2 mb-2 w-32 rounded-lg p-2.5 text-sm hover:bg-teal-800/80 text-center"
          >
            <LogOut className="w-4 h-4" />
            Sair
          </button>
        </div>
      </div>
    </aside>
  );

  return (
    <>
      <MobileHeader />
      <MobileMenu />
      <DesktopSidebar />
      <div className="h-16 lg:hidden" />
    </>
  );
};

export default UserSidebar;
