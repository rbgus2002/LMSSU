/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,
  async rewrites() {
    return [
      {
        source: '/apis/:path*',
        destination: `http://localhost:8080/:path*`,
      },
    ];
  },
}

module.exports = nextConfig
