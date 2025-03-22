import React from "react";
import Form from "@/app/(user)/form/page";

export default function DashBoard() {
  return (
    <div className="flex h-screen">
      <main className="flex-1 p-6 bg-gray-100 flex justify-center items-center">
        <Form />
      </main>
    </div>
  );
}
