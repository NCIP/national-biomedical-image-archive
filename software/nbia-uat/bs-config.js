var history = require('connect-history-api-fallback');
var connect = require('connect');

module.exports = {
    server: {
        baseDir: "./dist",
        middleware: {
            // overrides the second middleware default with new settings
            1: require('connect-history-api-fallback')({index: '/index.html', verbose: true})
        }
    }
};