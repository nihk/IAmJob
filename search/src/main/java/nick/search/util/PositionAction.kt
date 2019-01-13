package nick.search.util

import nick.data.model.Position

// TODO: Delete action?
sealed class PositionAction(val position: Position) {
    class Save(position: Position, val doSave: Boolean) : PositionAction(position)
    class MoreDetails(position: Position) : PositionAction(position)
}