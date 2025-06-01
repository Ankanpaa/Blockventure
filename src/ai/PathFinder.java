package ai;

import java.util.ArrayList;

import entity.Entity;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes() {
        
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void resetNodes() {

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;


    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity currentMonster) {
        resetNodes();
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                int tileNum = gp.tileM.mapTileNum[col][row];
                node[col][row].solid = gp.tileM.tile[tileNum].collision; // Mark solid tiles

                // Mark objects as solid
                for (Entity obj : gp.obj) {
                    if (obj != null) {
                        int objCol = obj.worldX / gp.tileSize;
                        int objRow = obj.worldY / gp.tileSize;
                        if (objCol == col && objRow == row && obj.collision) {
                            node[col][row].solid = true;
                        }
                    }
                }

                // Mark monsters as solid
                for (Entity monster : gp.monster) {
                    if (monster != null && monster.alive && monster != currentMonster) {
                        int left = (monster.worldX + monster.solidArea.x) / gp.tileSize;
                        int right = (monster.worldX + monster.solidArea.x + monster.solidArea.width - 1) / gp.tileSize;
                        int top = (monster.worldY + monster.solidArea.y) / gp.tileSize;
                        int bottom = (monster.worldY + monster.solidArea.y + monster.solidArea.height - 1) / gp.tileSize;

                        if (col >= left && col <= right && row >= top && row <= bottom) {
                            node[col][row].solid = true;
                        }
                    }
                }

                // Mark NPCs as solid
                for (Entity npc : gp.npc) {
                    if (npc != null) {
                        int nCol = npc.worldX / gp.tileSize;
                        int nRow = npc.worldY / gp.tileSize;
                        if (nCol == col && nRow == row && npc.alive) {
                            node[col][row].solid = true;
                        }
                    }
                }

                getCost(node[col][row]);
            }
        }
        if (goalCol < 0 || goalCol >= gp.maxWorldCol || goalRow < 0 || goalRow >= gp.maxWorldRow) {
            return;
        }
    }
    public void getCost(Node node) {
        
        int xDistance = Math.abs(node.col - goalNode.col);
        int yDistance = Math.abs(node.row - goalNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - startNode.col);
        yDistance = Math.abs(node.row - startNode.row);
        node.hCost = xDistance + yDistance;
        node.fCost = node.gCost + node.hCost;

    }
    public boolean search() {
        while (goalReached == false && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;
            currentNode.checked = true;
            openList.remove(currentNode);
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            if(col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }
            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            if(col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }else if (openList.get(i).fCost == bestNodefCost) {
                if(openList.get(i).hCost < openList.get(bestNodeIndex).hCost) {
                    bestNodeIndex = i;
                }
            }
            }

            if(openList.size() == 0) {
                break;
            }
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
        }
        step++;
        }
        return goalReached;
    }
    public void trackThePath() {
        
        Node current = goalNode;
        while(current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }

    }
    public void openNode(Node node) {

        if (node == null || node.solid || node.checked) return;

        if(node.open == false && node.checked == false && node.solid == false) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
            
            getCost(node);
            
        }
    }
}
