import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://34.204.49.54/:path*",
      },
    ];
  },
};

export default nextConfig;
