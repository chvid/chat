const Path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");

const config = {
    entry: "./src/index.js",
    output: {
        path: Path.resolve(__dirname, "dist"),
        filename: "bundle.js"
    },
    module: {
        rules: [
            { test: /\.(js|jsx)$/, use: "babel-loader", exclude: /node_modules/ },
            { test: /\.css$/, use: ["style-loader", "css-loader"] }
        ]
    },
    plugins: [new HtmlWebpackPlugin({ template: "index.html" })],
    performance: {
        maxEntrypointSize: 2048000,
        maxAssetSize: 2048000
    },
    devServer: {
        compress: true,
        port: 1112,
        proxy: {
            "/ws": {
                target: "ws://localhost:1111",
                ws: true
            }
        }
    },
    optimization: {
        minimize: true,
        minimizer: [
            new TerserPlugin({
                extractComments: false
            })
        ]
    }
};

module.exports = config;
