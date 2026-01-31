"use strict";
/**
 * HabitFlow Backend - Persona Service
 * Handles persona determination logic
 */
var _a;
Object.defineProperty(exports, "__esModule", { value: true });
exports.PersonaService = exports.personas = void 0;
var types_1 = require("../types");
exports.personas = (_a = {},
    _a[types_1.PersonaType.SUPPORTER] = {
        id: types_1.PersonaType.SUPPORTER,
        name: "The Supporter",
        title: "You're amazing! I believe in you today!",
        motivationMsg: "Every small step is giant progress. Keep going, you're on the right path!",
        completionMsg: "Excellent! You did it successfully. Great job on your persistence!",
        color: "#FF6B6B",
        emoji: "💪"
    },
    _a[types_1.PersonaType.CHALLENGER] = {
        id: types_1.PersonaType.CHALLENGER,
        name: "The Challenger",
        title: "Ready to conquer today? Don't let me down!",
        motivationMsg: "Today you're going to crush a new challenge. Don't fear the difficulty.",
        completionMsg: "Victory! You showed everyone who's boss. Amazing.",
        color: "#4ECDC4",
        emoji: "🔥"
    },
    _a);
var PersonaService = /** @class */ (function () {
    function PersonaService() {
    }
    /**
     * Determine persona based on questionnaire answers
     */
    PersonaService.determinePersona = function (answers) {
        // Simple logic: 'A' = Supporter, 'B' = Challenger
        var mainAnswer = answers.find(function (a) { return a.questionId === 'q1'; });
        if ((mainAnswer === null || mainAnswer === void 0 ? void 0 : mainAnswer.answer) === 'A') {
            return exports.personas[types_1.PersonaType.SUPPORTER];
        }
        else if ((mainAnswer === null || mainAnswer === void 0 ? void 0 : mainAnswer.answer) === 'B') {
            return exports.personas[types_1.PersonaType.CHALLENGER];
        }
        // Default to Supporter
        return exports.personas[types_1.PersonaType.SUPPORTER];
    };
    /**
     * Get all available personas
     */
    PersonaService.getAllPersonas = function () {
        return Object.values(exports.personas);
    };
    /**
     * Get persona by ID
     */
    PersonaService.getPersonaById = function (id) {
        return exports.personas[id] || null;
    };
    return PersonaService;
}());
exports.PersonaService = PersonaService;
