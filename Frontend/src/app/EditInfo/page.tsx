import Formedi from "@/components/EditInfo";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
export default function Login() {
  return (
    <main>
      <Header />
      <div className="h-full">
        <Formedi />
      </div>
      <Footer />
    </main>
  );
}