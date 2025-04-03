"use client";

import FormLogin from "@/components/FormLogin";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { getAuth, onAuthStateChanged } from "firebase/auth";

export default function Login() {
  const router = useRouter();

  useEffect(() => {
    const auth = getAuth();
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        router.push("/home");
      }
    });

    return () => unsubscribe();
  }, [router]);
  
  return (
    <main>
      <Header />
      <div className="h-full">
        <FormLogin />
      </div>
      <Footer />
    </main>
  );
}
