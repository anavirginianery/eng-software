import { defineConfig } from "eslint-define-config";
import globals from "globals";
import js from "@eslint/js";
import tseslint from "typescript-eslint";
import eslintPluginImport from "eslint-plugin-import";

export default defineConfig([
  js.configs.recommended,
  ...tseslint.configs.recommended,
  {
    files: ["src/**/*.ts", "src/**/*.tsx"],  // Alterado para src/**
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.node,
      },
      parserOptions: {
        project: "./tsconfig.json",  // Certifique-se que está na raiz do projeto
      },
    },
    plugins: {
      "@typescript-eslint": tseslint.plugin,
      "import": eslintPluginImport,
    },
    rules: {
      "no-console": "warn",
      "@typescript-eslint/no-unused-vars": "warn",
      "import/no-unresolved": ["error", { 
        ignore: ["^firebase/", "^@firebase/"] 
      }],
      "@typescript-eslint/consistent-type-imports": "warn",
    },
    settings: {
      "import/resolver": {
        typescript: {
          project: "./tsconfig.json"  // Especifica o caminho do tsconfig
        },
        node: true,
      },
    },
  },
  {
    ignores: [
      "node_modules/",
      "dist/",
      ".firebase/",
      "**/*.config.js",
      "firebase-debug.log",
      "firebase-export.json"
    ],
  },
]);