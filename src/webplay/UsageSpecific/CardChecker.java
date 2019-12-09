package webplay.UsageSpecific;

public class CardChecker {
	
	private String newline;
	private String resp;
	private String cards;
	
	public String cardChecker(String response){
		resp = response;
		cards = "";
		while(resp.length()>18){
			newline = resp.substring(resp.indexOf("\"card_id\":")+10,resp.indexOf("\"card_id\":")+17);
			switch(newline){
			case "1011120":cards+=" LR Black,";break;
			case "1010420":cards+=" LR Broly,";break;
			case "1010150":cards+=" LR Majin Vegeta,";break;
			case "1010050":cards+=" LR Gohan,";break;
			case "1011700":cards+=" LR Trunks,";break;
			case "1012140":cards+=" LR Bojack,";break;
			case "1011620":cards+=" LR Mighty Mask,";break;
			case "1007830":cards+=" LR SSJ3 Goku,";break;
			case "1014950":cards+=" LR Gofrieza,";break;
			case "1013750":cards+=" LR Beerus,";break;
			}
			resp = resp.substring(resp.indexOf("\"card_id\":")+18);
		}
		return cards;
	}
}
