# **Tabbula – Integration Guide**
## **📦 Package**
Tabbula offers a reusable and customizable tabbed layout using TabLayout and Fragment navigation.

---

## 📦 Installation

### Step 1. Add JitPack to `settings.gradle.kts` (in `dependencyResolutionManagement` block):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```
### Step 1. Add the dependency in your `build.gradle.kts`:

```
dependencies {
    implementation("com.github.hussein-yassine:Tabbula:v1.0.2")
}
```
---

## **✅ Features**
- Supports fixed and scrollable tab modes.
- Allows two loading strategies:
	- LOAD\_ALL\_AT\_ONCE: All fragments loaded initially and shown/hidden.
	- LOAD\_ONE\_BY\_ONE: Fragments are loaded lazily when first accessed.
	
## **🧠 Loading Strategies**
- LOAD\_ALL\_AT\_ONCE: All fragments are loaded at initialization and managed using show/hide.\
- LOAD\_ONE\_BY\_ONE: Fragments are added lazily when selected for the first time.

## **🚀 Integration Steps**
1\. Implement the core interface TabFragmentType in your App/SDK:


```
sealed class MyTabFragmentType : TabFragmentType {
	data object Dashboard : MyTabFragmentType()
	data object Leaderboard : MyTabFragmentType()
	data object Default : MyTabFragmentType()
}
```

2\. Define your Model:

```
data class TabbulaFragmentModel(
	val fragmentType: TabFragmentType,
	val tabTitle: String
)
```

3\. Construct a list of TabbulaFragmentModel:


```
val items = listOf(
		TabbedFragmentModel(MyTabFragmentType.Dashboard, "Dashboard"),
		TabbedFragmentModel(MyTabFragmentType.Leaderboard, "Leaderboard"),
		TabbedFragmentModel(MyTabFragmentType.Default, "Other")
)

```

4\. Initialize and populate the Tabbula Host component:

```
binding.tabbulaHost.initialize(
	fragmentManager = supportFragmentManager,
	loadingMethod = TabsLoadingMethod.LOAD_ONE_BY_ONE,
	listener = object : TabbulaHost.Listener {
		override fun selectedTabAtIndex(index: Int) {
			// Handle selection
		}
	}
)
```

5\. Populate the Tabs and Fragments

```
binding.tabbulaHost.populate(
	index = 0,
	list = items
) { tabType ->
	when (tabType) {
		is MyTabFragmentType.Dashboard -> DashboardTabFragment.newInstance()
		is MyTabFragmentType.Leaderboard -> LeaderboardFragment.newInstance()
		is MyTabFragmentType.Default -> EmptyStateFragment.newInstance()
		else -> throw IllegalArgumentException("Unknown tab type")
	}
}
```

## **🧩 Lifecycle Handling**
Make sure to clean up to avoid memory leaks

```
override fun onDestroyView() {
	super.onDestroyView()
	tabbedHost.destroy()
}
```

### **📌 Notes**
- The logic to create Fragment per TabFragmentType is **your responsibility** via the lambda passed to populate.
- TabbulaHost uses FragmentManager to handle fragment transactions under the hood.
- MAXIMUM\_FIXED\_TABS\_COUNT = 2 → auto-switches between FIXED and SCROLLABLE tab modes.
-----
### **🤝 Contributions**
If you think the TabbedHost can be extended further (custom indicators, swipe support, etc.), feel free to raise a proposal or open a PR.
##
