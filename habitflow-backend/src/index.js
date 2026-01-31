"use strict";
/**
 * HabitFlow Backend - Main Server
 * Express TypeScript server with AI integration
 */
Object.defineProperty(exports, "__esModule", { value: true });
var express_1 = require("express");
var cors_1 = require("cors");
var dotenv_1 = require("dotenv");
var api_1 = require("./routes/api");
// Load environment variables
dotenv_1.default.config();
var app = (0, express_1.default)();
var PORT = process.env.PORT || 3000;
// Middleware
app.use((0, cors_1.default)());
app.use(express_1.default.json());
app.use(express_1.default.urlencoded({ extended: true }));
// Request logging
app.use(function (req, res, next) {
    console.log("".concat(new Date().toISOString(), " - ").concat(req.method, " ").concat(req.path));
    next();
});
// API Routes
app.use('/api', api_1.default);
// Root endpoint
app.get('/', function (req, res) {
    res.json({
        message: 'HabitFlow AI Backend API',
        version: '1.0.0',
        endpoints: {
            health: '/api/health',
            personas: '/api/persona/all',
            determine: 'POST /api/persona/determine',
            motivation: 'POST /api/motivation/generate',
            recommendations: 'POST /api/habits/recommendations'
        }
    });
});
// 404 handler
app.use(function (req, res) {
    res.status(404).json({ error: 'Endpoint not found' });
});
// Error handler
app.use(function (err, req, res, next) {
    console.error('Error:', err);
    res.status(500).json({ error: 'Internal server error' });
});
// Start server
app.listen(PORT, function () {
    console.log("\uD83D\uDE80 HabitFlow Backend running on http://localhost:".concat(PORT));
    console.log("\uD83D\uDCDD API Documentation available at http://localhost:".concat(PORT, "/"));
    console.log("\u2705 Environment: ".concat(process.env.NODE_ENV || 'development'));
    if (process.env.OPENAI_API_KEY) {
        console.log('🤖 OpenAI API integration: ENABLED');
    }
    else {
        console.log('🤖 OpenAI API integration: DISABLED (using mock responses)');
    }
});
exports.default = app;
