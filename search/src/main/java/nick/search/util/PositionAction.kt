package nick.search.util

import nick.data.model.Position

sealed class PositionAction(val position: Position) {
    class SaveOrUnsave(position: Position) : PositionAction(position)
    class MoreDetails(position: Position) : PositionAction(position)
}