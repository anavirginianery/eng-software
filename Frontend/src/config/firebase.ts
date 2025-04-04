import { initializeApp } from "firebase/app";
import { connectAuthEmulator, getAuth } from "firebase/auth";
import { connectFirestoreEmulator, getFirestore } from "firebase/firestore";
import { connect } from "http2";

const firebaseConfig = {
  apiKey: "AIzaSyBpJTv3vhKLPu_jLu1yFlUBknkDm50jF3Y",
  authDomain: "diabetter-cab29.firebaseapp.com",
  projectId: "diabetter-cab29",
  storageBucket: "diabetter-cab29.firebasestorage.app",
  messagingSenderId: "469991945992",
  appId: "1:469991945992:web:de01615a65bbdbb2249ebd",
  measurementId: "G-YC3B8GK49R"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app); 

if (process.env.NODE_ENV === "development") {
  console.log("Firebase initialized in development mode");
  connectFirestoreEmulator(db, "localhost", 8080);

  connectAuthEmulator(auth, "http://127.0.0.1:9099");

}