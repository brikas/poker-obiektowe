package rounds;

import items.*;
import players.*;

public class Preflop {
	protected Player player;
	protected Computer computer;
	protected boolean dealer;
	protected Chips pot;		
	
	protected int playerBet;
	protected int computerBet;
	protected boolean allIn;

	public Preflop(Player player, Computer computer, boolean dealer, Chips pot) {
		this.player = player;
		this.computer = computer;
		this.dealer = dealer;
		this.pot = pot;
	}
	
	public void calculateBets(){
		if(dealer == true){
			if(player.getChips().getQuantity() == 0){
				pot.changeQuantity(-10);
				computer.getChips().changeQuantity(10 - pot.getQuantity());
				playerBet = pot.getQuantity();
				computerBet = pot.getQuantity();
				pot.changeQuantity(computerBet);
				allIn = true;
			}
			else if(computer.getChips().getQuantity() == 0){
				player.getChips().changeQuantity(5);
				pot.changeQuantity(-5);
				computerBet = pot.getQuantity();
				playerBet = computerBet > 5 ? 5 : computerBet;
				player.getChips().changeQuantity(-playerBet);
				pot.changeQuantity(playerBet);
				allIn = true;
			}
			else{
				playerBet = 5;
				computerBet = 10;
				allIn = false;
			}
		}
		else{
			if(computer.getChips().getQuantity() == 0){
				pot.changeQuantity(-10);
				player.getChips().changeQuantity(10 - pot.getQuantity());
				playerBet = pot.getQuantity();
				computerBet = pot.getQuantity();
				pot.changeQuantity(playerBet);
				allIn = true;
			}
			else if(player.getChips().getQuantity() == 0){
				computer.getChips().changeQuantity(5);
				pot.changeQuantity(-5);
				playerBet = pot.getQuantity();
				computerBet = playerBet > 5 ? 5 : playerBet;
				computer.getChips().changeQuantity(-computerBet);
				pot.changeQuantity(computerBet);
				allIn = true;
			}
			else{
				playerBet = 10;
				computerBet = 5;
				allIn = false;
			}
		}
	}
	
	public boolean start(){
		if((player.getChips().getQuantity() == 0 || computer.getChips().getQuantity() == 0) && playerBet == computerBet)
			return true;
		//------------Je�li gracz rozdawa�-----------------
		if(dealer == true){
			while(true){
				//-----------------Decyzja gracza------------------------
				int decision = player.decision(playerBet, computerBet, allIn);
				if(decision > 0){
					player.getChips().changeQuantity(playerBet - decision);
					pot.changeQuantity(decision - playerBet);
					playerBet = decision;
					if(player.getChips().getQuantity() == 0)
						allIn = true;
				}
				else if(decision == -2){
					playerFolds();
					return false;
				}
				else if(decision == -1){
					return true;
				}
				else if(decision == 0){
					playerCalls();
					if(playerBet != 10)
						return true;
				}
				if((player.getChips().getQuantity() == 0 || computer.getChips().getQuantity() == 0) && playerBet == computerBet)
					return true;
				showInfo();
				//----------------------Decyzja komputera-------------
				int decision2 = computer.decision(computerBet, playerBet, allIn);
				if(decision2 > 0){
					computer.getChips().changeQuantity(computerBet - decision2);
					pot.changeQuantity(decision2 - computerBet);
					computerBet = decision2;
					if(computer.getChips().getQuantity() == 0)
						allIn = true;
				}
				else if(decision2 == -2){
					computerFolds();
					return false;
				}
				else if(decision2 == -1){
					return true;
				}
				else if(decision2 == 0){
					computerCalls();
					return true;
				}
				if((player.getChips().getQuantity() == 0 || computer.getChips().getQuantity() == 0) && playerBet == computerBet)
					return true;
				showInfo();
			}
		}
		//---------Je�li komputer rozdawa�-------------------
		else{
			while(true){
				//-------------------Decyzja komputera-------------
				int decision2 = computer.decision(computerBet, playerBet, allIn);
				if(decision2 > 0){
					computer.getChips().changeQuantity(computerBet - decision2);
					pot.changeQuantity(decision2 - computerBet);
					computerBet = decision2;
					if(computer.getChips().getQuantity() == 0)
						allIn = true;
				}
				else if(decision2 == -2){
					computerFolds();
					return false;
				}
				else if(decision2 == -1){
					return true;
				}
				else if(decision2 == 0){
					computerCalls();
					if(computerBet!=10)
						return true;
				}
				if((player.getChips().getQuantity() == 0 || computer.getChips().getQuantity() == 0) && playerBet == computerBet)
					return true;
				showInfo();
				//-----------------Decyzja gracza------------------
				int decision = player.decision(playerBet, computerBet, allIn);
				if(decision > 0){
					player.getChips().changeQuantity(playerBet - decision);
					pot.changeQuantity(decision - playerBet);
					playerBet = decision;
					if(player.getChips().getQuantity() == 0)
						allIn = true;
				}
				else if(decision == -2){
					playerFolds();
					return false;
				}
				else if(decision == -1){
					return true;
				}
				else if(decision == 0){
					playerCalls();
					return true;
				}
				if((player.getChips().getQuantity() == 0 || computer.getChips().getQuantity() == 0) && playerBet == computerBet)
					return true;
				showInfo();
			}
		}
	}
	
	protected void showInfo(){
		System.out.print("Your chips: " + player.getChips().getQuantity());
		System.out.print("    " + computer.getName() + "'s chips: " + computer.getChips().getQuantity());
		System.out.println("    Pot: " + pot.getQuantity());
	}
	
	protected void playerFolds(){
		computer.getChips().changeQuantity(pot.getQuantity());
		System.out.println(computer.getName() + " collects " + pot.getQuantity() + " from pot");
		pot.setQuantity(0);
	}
	
	protected void computerFolds(){
		player.getChips().changeQuantity(pot.getQuantity());
		System.out.println(player.getName() + " collects " + pot.getQuantity() + " from pot");
		pot.setQuantity(0);
	}
	
	protected void playerCalls(){
		if(player.getChips().getQuantity() + playerBet > computerBet){
			player.getChips().changeQuantity(playerBet - computerBet);
			pot.changeQuantity(computerBet - playerBet);
			playerBet = computerBet;
		}
		else{
			pot.changeQuantity(player.getChips().getQuantity());
			playerBet += player.getChips().getQuantity();
			player.getChips().setQuantity(0);
			computer.getChips().changeQuantity(computerBet - playerBet);
			pot.changeQuantity(-computerBet);
			computerBet = playerBet;
			pot.changeQuantity(computerBet);
		}
	}
	
	protected void computerCalls(){
		if(computer.getChips().getQuantity() + computerBet > playerBet){
			computer.getChips().changeQuantity(computerBet - playerBet);
			pot.changeQuantity(playerBet - computerBet);
			computerBet = playerBet;
		}
		else{
			pot.changeQuantity(computer.getChips().getQuantity());
			computerBet += computer.getChips().getQuantity();
			computer.getChips().setQuantity(0);
			player.getChips().changeQuantity(playerBet - computerBet);
			pot.changeQuantity(-playerBet);
			playerBet = computerBet;
			pot.changeQuantity(playerBet);
		}
	}
}
