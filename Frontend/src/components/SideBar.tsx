"use client";

import React, { useState } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  Home,
  User,
  Activity,
  BarChart2,
  X,
  Menu,
} from "lucide-react";

interface SidebarProps {
  className?: string;
}

const AdvisorSidebar: React.FC<SidebarProps> = ({ className = "" }) => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const pathname = usePathname();

  const isActive = (path: string) => pathname.startsWith(path);

  const toggleMobileMenu = () => setIsMobileMenuOpen(!isMobileMenuOpen);

  const ProfileSection = () => (
    <div className="flex flex-col items-center px-4 pt-8 pb-6">
      <div className="w-24 h-24 rounded-full bg-gray-200 flex items-center justify-center mb-3">
        <User className="w-12 h-12 text-gray-400" />
      </div>
      <span className="text-gray-500 text-sm mb-4">Nome do Usuário</span>
    </div>
  );

  const MobileHeader = () => (
    <header className="fixed top-0 left-0 right-0 z-20 flex items-center justify-end h-16 px-4 bg-[#FFFFFF] border-b border-gray-300 lg:hidden">
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
          <ProfileSection />
          <nav className="flex-1 p-4 space-y-1">
            <Link
              href="/home"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <Home className="w-4 h-4" />
              <span>Home</span>
            </Link>

            <Link
              href="/perfil"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <User className="w-4 h-4" />
              <span>Perfil</span>
            </Link>

            <Link
              href="/medicoes"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <Activity className="w-4 h-4" />
              <span>Medições</span>
            </Link>

            <Link
              href="/dashboard"
              className="flex items-center gap-2 p-2 rounded-lg text-sm font-medium text-gray-800 hover:text-white hover:bg-teal-800/80"
              onClick={toggleMobileMenu}
            >
              <BarChart2 className="w-4 h-4" />
              <span>Dashboard</span>
            </Link>
          </nav>
        </div>
      </div>
    );

  const DesktopSidebar = () => (
    <aside
      className={`fixed top-0 left-0 bottom-0 w-64 bg-[#FFFFFF] border-r border-gray-300 hidden lg:block ${className}`}
    >
      <ProfileSection />
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
          <Home className="w-4 h-4" />
          <span>Home</span>
        </Link>

        <Link
          href="/perfil"
          className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/perfil")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
        >
          <User className="w-4 h-4" />
          <span>Perfil</span>
        </Link>

        <Link
          href="/inserirDados"
          className={`
            flex items-center gap-2 p-2 rounded-lg
            text-sm font-medium transition-colors
            ${
              isActive("/medicoes")
                ? "text-white bg-teal-800/80"
                : "text-gray-800 hover:text-white hover:bg-teal-800/80"
            }
          `}
        >
          <Activity className="w-4 h-4" />
          <span>Medições</span>
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
          <BarChart2 className="w-4 h-4" />
          <span>Dashboard</span>
        </Link>
      </nav>
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

export default AdvisorSidebar;
