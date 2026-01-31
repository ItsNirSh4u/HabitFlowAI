# HabitFlow Unified Backend - Quick Start Script
# This script helps you set up and run the unified backend

Write-Host "🚀 HabitFlow Unified Backend Setup" -ForegroundColor Cyan
Write-Host "====================================`n" -ForegroundColor Cyan

# Check if Node.js is installed
try {
    $nodeVersion = node --version
    Write-Host "✅ Node.js detected: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Node.js is not installed!" -ForegroundColor Red
    Write-Host "   Please install Node.js from https://nodejs.org/" -ForegroundColor Yellow
    exit 1
}

# Check if we're in the unified-backend directory
if (-not (Test-Path "package.json")) {
    Write-Host "❌ Please run this script from the unified-backend directory" -ForegroundColor Red
    exit 1
}

# Install dependencies
Write-Host "`n📦 Installing dependencies..." -ForegroundColor Yellow
npm install

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Failed to install dependencies" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Dependencies installed successfully" -ForegroundColor Green

# Check if .env exists
if (-not (Test-Path ".env")) {
    Write-Host "`n⚠️  No .env file found. Creating from template..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
    Write-Host "✅ Created .env file" -ForegroundColor Green
    Write-Host "`n⚙️  Please edit .env file with your configuration:" -ForegroundColor Cyan
    Write-Host "   - Set MONGO_URI for database connection" -ForegroundColor White
    Write-Host "   - Set JWT_SECRET for authentication" -ForegroundColor White
    Write-Host "   - Set OPENAI_API_KEY for AI features (optional)" -ForegroundColor White
    Write-Host "   - Set GEMINI_API_KEY for advanced classification (optional)" -ForegroundColor White
    Write-Host ""
    
    $response = Read-Host "Do you want to edit .env now? (y/n)"
    if ($response -eq "y" -or $response -eq "Y") {
        notepad .env
    }
}

# Check MongoDB connection
Write-Host "`n🗄️  Checking MongoDB configuration..." -ForegroundColor Yellow
$envContent = Get-Content ".env" -Raw
if ($envContent -match "MONGO_URI=(.+)") {
    $mongoUri = $matches[1].Trim()
    if ($mongoUri -eq "" -or $mongoUri -like "*mongodb://*") {
        Write-Host "⚠️  MongoDB URI configured but not validated" -ForegroundColor Yellow
        Write-Host "   Make sure MongoDB is running if you want authentication features" -ForegroundColor White
    }
} else {
    Write-Host "⚠️  MONGO_URI not set - authentication will be disabled" -ForegroundColor Yellow
}

# Check JWT_SECRET
if ($envContent -match "JWT_SECRET=(.+)") {
    $jwtSecret = $matches[1].Trim()
    if ($jwtSecret -eq "") {
        Write-Host "⚠️  JWT_SECRET not set - authentication will fail!" -ForegroundColor Yellow
        Write-Host "   Generate one with: openssl rand -base64 32" -ForegroundColor White
    } else {
        Write-Host "✅ JWT_SECRET configured" -ForegroundColor Green
    }
}

# Build the project
Write-Host "`n🔨 Building TypeScript..." -ForegroundColor Yellow
npm run build

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Build successful" -ForegroundColor Green

# Show next steps
Write-Host "`n✨ Setup Complete!" -ForegroundColor Green
Write-Host "================`n" -ForegroundColor Green

Write-Host "To start the development server:" -ForegroundColor Cyan
Write-Host "  npm run dev`n" -ForegroundColor White

Write-Host "To start the production server:" -ForegroundColor Cyan
Write-Host "  npm start`n" -ForegroundColor White

Write-Host "Server will run on: http://localhost:3000" -ForegroundColor Yellow
Write-Host "API docs will be at: http://localhost:3000/`n" -ForegroundColor Yellow

Write-Host "For Android emulator, use: http://10.0.2.2:3000/" -ForegroundColor Magenta
Write-Host ""

$startNow = Read-Host "Do you want to start the development server now? (y/n)"
if ($startNow -eq "y" -or $startNow -eq "Y") {
    Write-Host "`n🚀 Starting development server...`n" -ForegroundColor Cyan
    npm run dev
}
