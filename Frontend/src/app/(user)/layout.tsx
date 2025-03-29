"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import SideBar from "@/components/SideBar";

export default function UserLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();

  useEffect(() => {
    const usuario = localStorage.getItem("usuario");
    if (!usuario) {
      router.push("/login");
    }
  }, [router]);

  return (
    <div>
      <SideBar />
      <main className="lg:pl-64">
        <div className="max-w-8xl mx-auto">{children}</div>
      </main>
    </div>
  );
}
