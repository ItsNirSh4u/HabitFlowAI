"use strict";
/**
 * HabitFlow Backend - AI Service
 * Handles AI-powered motivation messages and recommendations
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AIService = void 0;
var axios_1 = require("axios");
var types_1 = require("../types");
var personaService_1 = require("./personaService");
var AIService = /** @class */ (function () {
    function AIService() {
    }
    /**
     * Generate AI-powered motivation message
     */
    AIService.generateMotivation = function (request) {
        return __awaiter(this, void 0, void 0, function () {
            var persona, prompt_1, response, aiMessage, error_1;
            var _a, _b;
            return __generator(this, function (_c) {
                switch (_c.label) {
                    case 0:
                        persona = personaService_1.PersonaService.getPersonaById(request.personaId);
                        if (!persona) {
                            throw new Error('Invalid persona ID');
                        }
                        // If OpenAI API key is not configured, return enhanced mock response
                        if (!this.OPENAI_API_KEY) {
                            return [2 /*return*/, this.getMockMotivation(request, persona)];
                        }
                        _c.label = 1;
                    case 1:
                        _c.trys.push([1, 3, , 4]);
                        prompt_1 = this.buildPrompt(request, persona);
                        return [4 /*yield*/, axios_1.default.post(this.OPENAI_ENDPOINT, {
                                model: 'gpt-3.5-turbo',
                                messages: [
                                    {
                                        role: 'system',
                                        content: "You are a motivational coach with the personality of \"".concat(persona.name, "\". Your style is: ").concat(persona.motivationMsg, ". Keep responses under 50 words, uplifting and actionable.")
                                    },
                                    {
                                        role: 'user',
                                        content: prompt_1
                                    }
                                ],
                                max_tokens: 100,
                                temperature: 0.8
                            }, {
                                headers: {
                                    'Authorization': "Bearer ".concat(this.OPENAI_API_KEY),
                                    'Content-Type': 'application/json'
                                }
                            })];
                    case 2:
                        response = _c.sent();
                        aiMessage = ((_b = (_a = response.data.choices[0]) === null || _a === void 0 ? void 0 : _a.message) === null || _b === void 0 ? void 0 : _b.content) || persona.motivationMsg;
                        return [2 /*return*/, {
                                message: aiMessage.trim(),
                                personaId: request.personaId,
                                timestamp: new Date().toISOString()
                            }];
                    case 3:
                        error_1 = _c.sent();
                        console.error('OpenAI API error:', error_1);
                        // Fallback to enhanced mock response
                        return [2 /*return*/, this.getMockMotivation(request, persona)];
                    case 4: return [2 /*return*/];
                }
            });
        });
    };
    /**
     * Generate habit recommendations based on completion history
     */
    AIService.generateHabitRecommendations = function (personaId, completedHabits, context) {
        return __awaiter(this, void 0, void 0, function () {
            var allHabits, available;
            return __generator(this, function (_a) {
                allHabits = [
                    'Drink a glass of water',
                    'Physical activity',
                    'Read for five minutes',
                    'Meditate for 5 minutes',
                    'Practice gratitude',
                    'Take a break and stretch'
                ];
                available = allHabits.filter(function (h) { return !completedHabits.includes(h); });
                // Return top 3 recommendations
                return [2 /*return*/, available.slice(0, 3)];
            });
        });
    };
    /**
     * Build AI prompt based on request context
     */
    AIService.buildPrompt = function (request, persona) {
        var prompt = "Generate a motivational message for someone working on their daily habits.";
        if (request.habitsCompleted !== undefined && request.totalHabits !== undefined) {
            prompt += " They have completed ".concat(request.habitsCompleted, " out of ").concat(request.totalHabits, " habits today.");
        }
        if (request.context) {
            prompt += " Context: ".concat(request.context);
        }
        return prompt;
    };
    /**
     * Get enhanced mock motivation (when API key not available)
     */
    AIService.getMockMotivation = function (request, persona) {
        var _a;
        var messages = (_a = {},
            _a[types_1.PersonaType.SUPPORTER] = [
                "You're doing amazing! Each habit brings you closer to your best self. Keep that positive energy flowing! 💪",
                "I'm so proud of your dedication! Remember, progress over perfection. You've got this!",
                "Your commitment is inspiring! Every small win is a step toward greatness. Believe in yourself!"
            ],
            _a[types_1.PersonaType.CHALLENGER] = [
                "Time to dominate! Show those habits who's boss. No excuses, just results! 🔥",
                "You didn't come this far to only come this far. Push harder, aim higher, crush it!",
                "Winners find ways, losers find excuses. You're a winner. Prove it today!"
            ],
            _a);
        var personaMessages = messages[request.personaId] || messages[types_1.PersonaType.SUPPORTER];
        var randomMessage = personaMessages[Math.floor(Math.random() * personaMessages.length)];
        return {
            message: randomMessage,
            personaId: request.personaId,
            timestamp: new Date().toISOString()
        };
    };
    AIService.OPENAI_API_KEY = process.env.OPENAI_API_KEY;
    AIService.OPENAI_ENDPOINT = 'https://api.openai.com/v1/chat/completions';
    return AIService;
}());
exports.AIService = AIService;
