# LuisaMineReset

A lightweight and modern mine reset plugin for Paper servers.

LuisaMineReset allows you to create configurable mines using WorldEdit selections, define custom block distributions, schedule automatic resets, and manually reset mines through simple commands.

## Features

- WorldEdit integration
- Automatic mine reset
- Manual mine reset
- Configurable reset timer
- Custom block percentages
- Mine spawn point
- Translation support via messages.yml
- Permission system
- Tab completion
- Lightweight and easy to configure

## Requirements

- Java 21+
- Paper 1.21+
- WorldEdit

## Installation

1. Download the latest release.
2. Place the plugin inside the `plugins` folder.
3. Restart the server.
4. Configure the plugin if desired.

## Commands

| Command | Description |
|----------|-------------|
| `/lmr help` | Show help |
| `/lmr create <mine>` | Create a mine |
| `/lmr delete <mine>` | Delete a mine |
| `/lmr rename <old> <new>` | Rename a mine |
| `/lmr list` | List all mines |
| `/lmr setregion <mine>` | Set mine region |
| `/lmr setblock <mine> ...` | Configure blocks |
| `/lmr settime <mine> <seconds>` | Set auto reset time |
| `/lmr setspawn <mine>` | Set mine spawn |
| `/lmr reset <mine>` | Reset mine |
| `/lmr info <mine>` | Show mine information |
| `/lmr reload` | Reload configuration |

## Permissions

| Permission | Description |
|------------|-------------|
| `luisaminereset.help` | Use help |
| `luisaminereset.create` | Create mines |
| `luisaminereset.delete` | Delete mines |
| `luisaminereset.rename` | Rename mines |
| `luisaminereset.list` | List mines |
| `luisaminereset.setregion` | Set mine region |
| `luisaminereset.setblock` | Configure blocks |
| `luisaminereset.settime` | Configure auto reset |
| `luisaminereset.setspawn` | Set mine spawn |
| `luisaminereset.reset` | Reset mines |
| `luisaminereset.info` | View mine information |
| `luisaminereset.reload` | Reload plugin |
| `luisaminereset.admin` | Access all commands |

## Configuration

The plugin stores its data in:

```
plugins/LuisaMineReset/
```

Files:

- config.yml
- messages.yml
- mines.yml

## Support

If you find a bug or have a suggestion, please open an Issue on GitHub.

## License

MIT License

## Author

Paulo Bernardes
