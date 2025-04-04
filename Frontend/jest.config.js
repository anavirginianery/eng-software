/** @type {import('ts-jest').JestConfigWithTsJest} */
module.exports = {
  testEnvironment: "node", // ou "jsdom" se usar DOM
  transform: {
    "^.+\\.tsx?$": ["ts-jest", {}],
  },
};
