# TandemCommunity

A community member listing Android app built with modern Android development practices. Displays paginated community members from a remote API with local like-state persistence.

## Architecture

**Single module** with layered architecture following Clean Architecture principles. Given the small scale of the project, a single module was sufficient. For larger scales, the project would transition to a **multi-module** setup with separate modules for `:core`, `:data`, `:domain`, and `:feature:community`.

```
com.sajjadfatehi.tandemcommunity
├── core/                  # Shared utilities (Result wrapper)
├── data/                  # Data layer
│   ├── local/             # Room database, DAOs, entities
│   ├── mapper/            # DTO → Domain model mappers
│   ├── paging/            # PagingSource implementation
│   ├── remote/            # API service, DTOs, data source
│   └── repository/        # Repository implementations
├── di/                    # Hilt dependency injection modules
├── domain/                # Domain layer
│   ├── datasource/        # Data source interfaces
│   ├── model/             # Domain models
│   ├── repository/        # Repository interfaces
│   └── usecase/           # Use cases
├── presentation/          # UI layer (Compose screens, ViewModel)
└── ui/theme/              # Material 3 theming
```

### Layer Responsibilities

| Layer | Responsibility |
|-------|---------------|
| **Presentation** | Compose UI, ViewModel, user actions, UI state |
| **Domain** | Use cases, domain models, repository/data source interfaces |
| **Data** | API calls, Room database, paging, mappers, repository impl |

## Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + Clean Architecture (single module) |
| **DI** | Hilt |
| **Networking** | Retrofit + OkHttp + Kotlinx Serialization |
| **Pagination** | Paging 3 (`PagingSource` — no RemoteMediator) |
| **Database** | Room (like-state persistence only) |
| **Image Loading** | Coil 3 |
| **Testing** | JUnit 4 + MockK + Coroutines Test |

## Data Flow

```
API (JSON files) → Retrofit → CommunityRemoteDataSourceImpl
    → PagingSource → Pager → cachedIn(viewModelScope)
    → combine(likedMemberIds) → CommunityScreen (LazyColumn)

Room (liked state) → CommunityLocalDataSourceImpl
    → Repository → ViewModel → combine with paging data
```

## Pagination

The app uses Paging 3 with a custom `PagingSource` that fetches pages directly from the API. No local caching of member data — only the like state is persisted in Room.

### Error Handling in Pagination

| Scenario | Handling |
|----------|----------|
| **API success** | Returns `LoadResult.Page` with data, `prevKey`, `nextKey` |
| **API 404 (end of pages)** | Returns `Result.Success(emptyList())` → `LoadResult.Page` with `nextKey = null` |
| **Network error (IOException)** | Returns `LoadResult.Error` — Paging library shows error state + retry |
| **Other HTTP errors** | Returns `LoadResult.Error` with descriptive message |
| **Empty result (no data)** | UI shows `EmptyResult` composable with illustration |
| **Refresh error** | UI shows `ErrorView` with retry button |

### Pagination Config

```kotlin
PagingConfig(
    pageSize = 20,
    initialLoadSize = 20,
    prefetchDistance = 3,
    enablePlaceholders = false
)
```

## Like State

Likes are persisted locally using Room. The `LikedCommunityMemberEntity` stores only the member ID. The `combine` operator in the ViewModel merges paging data with liked IDs on each emission, so like state updates are reflected without re-fetching from the API.

## Dependency Injection (Hilt)

| Module | Provides |
|--------|----------|
| `NetworkModule` | Retrofit, OkHttpClient, Json, ApiClient |
| `DatabaseModule` | Room database, DAOs |
| `CommunityModule` | Repository, data source bindings |

## Testing

Unit tests cover the data and domain layers:

| Test Class | What It Tests |
|------------|--------------|
| `CommunityViewModelTest` | ViewModel actions, use case interactions |
| `GetCommunityUseCaseTest` | Use case delegation to repository |
| `ObserveLikedMemberIdsUseCaseTest` | Use case delegation to repository |
| `ToggleLikeUseCaseTest` | Like toggle logic |
| `CommunityRepositoryImplTest` | Repository delegation to data sources |
| `CommunityMemberPagingSourceTest` | Paging load success/error/empty scenarios |
| `CommunityRemoteDataSourceImplTest` | API success/404/500/network error handling |
| `CommunityLocalDataSourceImplTest` | DAO delegation for like operations |

## Scaling Strategy

### Multi-Module (larger scale)

```
:app                    # Application module (DI, navigation)
:core:network           # Retrofit, OkHttp, API abstractions
:core:database          # Room, DAOs, entities
:core:common            # Result, shared utilities
:feature:community      # Community feature (UI, ViewModel, PagingSource)
```

### Compose Multiplatform (cross-platform)

To scale to other platforms (iOS, Desktop, Web), the project would use Compose Multiplatform with a `commonMain` directory:

```
src/
├── commonMain/          # Shared code (ViewModel, UseCases, Repository, PagingSource, Models)
│   ├── domain/
│   ├── data/
│   └── presentation/
├── androidMain/         # Android-specific (Room, Retrofit, Hilt)
├── iosMain/             # iOS-specific (Ktor, SQLDelight)
└── desktopMain/         # Desktop-specific
```

Most business logic, use cases, repository interfaces, domain models, and even Compose UI would live in `commonMain`. Only platform-specific implementations (networking, database, DI) would remain in platform directories.

## R8 / ProGuard

R8 is enabled for release builds with `isMinifyEnabled = true` and `isShrinkResources = true`. ProGuard rules keep:

- `@Serializable` DTO classes and their generated serializers (Kotlinx Serialization)
- `@SerialName` annotations for correct JSON field mapping
- Room entities and DAOs (handled automatically by Room + KSP)

## API

Base URL: `https://tandem2019.web.app/api/`

| Endpoint | Response |
|----------|----------|
| `community_{page}.json` | `CommunityMemberResponseDto` with list of members |
| `community_5.json` (non-existent) | HTTP 404 |

## Build

```bash
# Debug
./gradlew assembleDebug

# Release (with R8)
./gradlew assembleRelease

# Run tests
./gradlew testDebugUnitTest
```
