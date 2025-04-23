import Formcad from "@/components/CadInfo";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
export default function Login() {
  return (
    <main>
      <Header />
      <div className="h-full">
        <Formcad />
      </div>
      <Footer />
    </main>
  );
}