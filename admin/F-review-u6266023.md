Reviewer: Cen Dai (u6266023)
Component: <Board.java;Function name: MakeGrid, showDialog>
            <PieceRotateTest>
Author: Ziwei Wang (u6787586)


Review Comments:

1. Board.java: line 557-573, it's a good idea to use our lab content to generate a baseboard.
               line 646-652, a good method to provide reminding messages for the player,
               it could be called in other method when we need to give the player a message.         
2. PieceRotateTest.java: This test can go through all the pieces and all the rotation status.
                It could pass a correct testing result. However, it could be optimized. Instead
                of writing 8 tests, building another method passing through the id of the piece,
                then running the test iteratively is a more efficient way.