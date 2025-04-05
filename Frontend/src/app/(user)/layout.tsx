"use client";

import { useEffect } from "react";
import { useRouter, usePathname } from "next/navigation";
import { getAuth, onAuthStateChanged } from "firebase/auth";
import { doc, getDoc } from "firebase/firestore";
import { db } from "@/config/firebase";
import SideBar from "@/components/SideBar";

export default function UserLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const auth = getAuth();
    
    onAuthStateChanged(auth, async (user) => {
      if (!user) {
        router.push("/login");
        return;
      }

      try {
        const userDoc = await getDoc(doc(db, "usuarios", user.uid));
        if (!userDoc.exists()) {
          router.push("/login");
          return;
        }

        const userData = userDoc.data();
        const cadastroCompleto = userData.cadastroCompleto === true;
        
        if (!cadastroCompleto && pathname !== "/form") {
          router.push("/form");
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
        router.push("/login");
      }
    });
  }, [router, pathname]);

  return (
    <div>
      <SideBar />
      <main className="lg:pl-64">
        <div className="max-w-8xl mx-auto">{children}</div>
      </main>
    </div>
  );
}
