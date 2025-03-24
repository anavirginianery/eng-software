
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
import FormCadastro from "@/components/FormCadastro";
export default function Login() {
  return (
    <main>
      <Header />
      <div className="h-full">
        <FormCadastro />
      </div>
      <Footer />
    </main>
  );
}
