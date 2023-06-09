/**
 * Created by Jacky.Gao on 2018-04-15.
 * Base on Webpack4
 */
const path = require('path');
module.exports = {
    mode: 'development',
    entry: {
        designer: './src/index.js',
        searchform: './src/form/index.js',
        preview: './src/preview.js'
    },
    output: {
        path: path.resolve('../xreport-action/src/main/resources/xreport-asserts/js'),
        filename: '[name].bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: "babel-loader"
            },
            {
                test: /\.css$/,
                use: [{loader: 'style-loader'}, {loader: 'css-loader'}]
            },
            {
                test: /\.(eot|woff|woff2|ttf|svg|png|jpg)$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            limit: 10000000
                        }
                    }
                ]
            }
        ]
    }
};