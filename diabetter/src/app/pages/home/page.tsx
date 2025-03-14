export default function Home() {
  return (
    <main className="bg-gray-100 min-h-screen flex flex-col items-center">
      <section className="w-full max-w-3xl p-6 text-center">
        <h1 className="text-3xl font-bold">Keep your glucose <span className="text-teal-600">under control</span></h1>
        <p className="mt-4 text-gray-600">
          Plataforma intuitiva para pessoas com diabetes registrarem suas taxas de glicose e dados de saúde essenciais, simplificando o monitoramento diário.
        </p>
        <div className="mt-6 flex justify-center space-x-4">
          <button className="bg-teal-600 text-white px-6 py-2 rounded">Começar agora</button>
          <button className="border border-teal-600 text-teal-600 px-6 py-2 rounded">Saiba Mais</button>
        </div>
      </section>
      
      <section className="w-full max-w-3xl p-6">
        <h2 className="text-2xl font-bold text-center">Principais Funcionalidades</h2>
        <div className="mt-6 space-y-4">
          <div className="bg-white shadow p-4 rounded flex items-center">
            <div className="w-10 h-10 bg-teal-100 flex items-center justify-center rounded-full">📋</div>
            <div className="ml-4">
              <h3 className="font-bold">Registros Detalhados</h3>
              <p className="text-gray-600">Acompanhe suas taxas de glicose e insulina de forma organizada e intuitiva.</p>
            </div>
          </div>
          <div className="bg-white shadow p-4 rounded flex items-center">
            <div className="w-10 h-10 bg-teal-100 flex items-center justify-center rounded-full">📊</div>
            <div className="ml-4">
              <h3 className="font-bold">Relatórios Completos</h3>
              <p className="text-gray-600">Visualize sua evolução através de gráficos e relatórios detalhados.</p>
            </div>
          </div>
          <div className="bg-white shadow p-4 rounded flex items-center">
            <div className="w-10 h-10 bg-teal-100 flex items-center justify-center rounded-full">💬</div>
            <div className="ml-4">
              <h3 className="font-bold">Compartilhamento Médico</h3>
              <p className="text-gray-600">Compartilhe seus dados facilmente com sua equipe médica.</p>
            </div>
          </div>
        </div>
      </section>
      
      <footer className="w-full p-4 text-center text-gray-500 text-sm">
        &copy; 2025 Diabetter. Todos os direitos reservados.
      </footer>
    </main>
  );
}