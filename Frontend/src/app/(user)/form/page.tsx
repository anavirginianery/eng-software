import React from "react";
import KeyFilter from "@/components/KeyFilter";

export default function Form() {
  return (
    <main>
      <div className="flex flex-col items-center justify-center h-screen px-4 sm:px-6 lg:px-8 py-6">
        <KeyFilter />
      </div>
    </main>
  );
}
