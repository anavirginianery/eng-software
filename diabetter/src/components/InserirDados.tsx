
export default function Form() {
  return (
      <form>
          <div className="mb-8 mt-8">
              <label className="block text-sm font-normal text-gray-700">Insulina</label>
              <input 
                  type="text" 
                  className="w-full p-2 bg-gray-200 rounded mt-1"
              />
          </div>
          
          <div className="mb-6">
              <label className="block text-sm font-normal text-gray-700">Horário</label>
              <input 
                  type="text" 
                  className="w-full p-2 bg-gray-200 rounded mt-1"
              />
          </div>
          
          <button 
              type="submit" 
              className="w-full bg-[#38B2AC] text-white py-2 rounded"
          >
              Inserir
          </button>
      </form>
  );
}