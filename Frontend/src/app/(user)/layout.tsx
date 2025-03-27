import SideBar from "@/components/SideBar";

export default function UserLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div>
      <SideBar />
      <main className="lg:pl-64">
        <div className="max-w-8xl mx-auto">{children}</div>
      </main>
    </div>
  );
}
