# Contributing to Vidiary

Thank you for considering contributing to **Vidiary** 🎥
This project is a lightweight Android video journal app focused on simplicity and privacy.

---

## Getting Started

Before you start working on the project:

1. Fork the repository
2. Clone your fork locally:

```bash
git clone https://github.com/your-username/VideoJournalApp.git
```

3. Open the project in **Android Studio**
4. Sync Gradle and ensure the project builds successfully

---

## Project Structure

The project follows a modular Android architecture:

* **UI**: Jetpack Compose screens and components
* **Data**: SQLDelight database layer
* **DI**: Koin modules
* **Media**: CameraX + Media3 ExoPlayer integration

---

## Branch Naming Convention

Use descriptive branch names:

* `feature/add-video-editing`
* `feature/improve-feed-ui`
* `fix/camera-crash-on-start`
* `refactor/video-database-layer`

---

## Coding Guidelines

Please follow these conventions:

* Use **Kotlin coding standards**
* Follow **Jetpack Compose best practices**
* Keep composables small and reusable
* Avoid business logic inside UI components
* Prefer clean architecture separation (UI / domain / data)
* Use meaningful variable and function names

---

## Commit Message Style

Use clear and consistent commit messages:

```
feat: add video recording feature
fix: resolve crash on camera launch
refactor: improve feed performance
docs: update README
```

---

## Pull Request Process

1. Ensure your code builds without errors
2. Test your changes on a physical device or emulator
3. Keep PRs small and focused
4. Link related issues in the PR description
5. Provide a clear description of changes

---

## Reporting Issues

If you find a bug or have a feature request:

* Open an issue on GitHub
* Describe the problem clearly
* Include steps to reproduce (if applicable)
* Add screenshots or logs if helpful

---

## Code of Conduct

Be respectful and constructive.
This is a learning-focused and educational project.

---

## Thank You

Your contributions help improve Vidiary and make it more useful for everyone 🚀
