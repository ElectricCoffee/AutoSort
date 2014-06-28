package io.wausoft.data

/* Example of a settings file:
 * {
 *   "auto-sort-folder-path": "C:\Users\ElectricCoffee\Desktop\AutoSort",
 *   "local-settings": [{
 *     "source-program": null,
 *     "file-types": ["iso", "dmg"],
 *     "destination-path": "C:\Users\ElectricCoffee\DiskImages",
 *     "keywords": []
 *   }]
 * }
 */

case class SettingsContainer(autoSortFolderPath: String, localSettings: List[LocalSettingsContainer])
case class LocalSettingsContainer(sourceProgram: Option[String], fileTypes: List[String], destinationPath: String, keywords: List[String])
