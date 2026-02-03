#!/bin/bash

echo "Setting up Gongwuyuan React Frontend..."

# Stop any existing processes
pkill -f "node" 2>/dev/null || true
pkill -f "react-scripts" 2>/dev/null || true

# Ensure we have the necessary dependencies
cd /home/zhuyinhang/.openclaw/workspace/gongwuyuan-app/frontend/web

echo "Installing dependencies..."
npm install --legacy-peer-deps --silent

# Install additional required packages
npm install webpack webpack-dev-server --save-dev --legacy-peer-deps --silent

# Create a minimal webpack configuration
cat > webpack.config.js << 'EOF'
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: './src/index.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'bundle.js',
    publicPath: '/'
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react']
          }
        }
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
      }
    ]
  },
  resolve: {
    extensions: ['.js', '.jsx'],
  },
  devServer: {
    port: 3000,
    hot: true,
    open: true,
    historyApiFallback: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      }
    }
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './public/index.html'
    })
  ]
};
EOF

# Create a basic package.json with webpack scripts if needed
if ! grep -q "webpack" package.json; then
  # Backup original package.json
  cp package.json package.json.backup
  
  # Add webpack scripts
  jq '.scripts.start = "webpack serve --mode development"' package.json.backup > package.json.tmp && mv package.json.tmp package.json
  jq '.scripts.build = "webpack --mode production"' package.json > package.json.tmp && mv package.json.tmp package.json
fi

echo "Starting React development server..."
npm start