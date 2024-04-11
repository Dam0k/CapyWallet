**CapyWallet je velice jednoducha "financni" aplikace na trackovani vasich vydaju.**

Tato dokumentace slouzi k predstaveni jejich funkci a ovladacich prvku

![image](https://github.com/Dam0k/CapyWallet/assets/77647982/1438783a-78d6-472e-a467-d7b618afba7e)

Toto je uvodni aktivita aplikace pri prvni instalaci. 
Je zde toolbar s nazvem aplikace "CapyWallet" a jejim logem 
pod nim je text s rozpoctem ktery si uzivatel muze sam nastavit v tlacitku profile (defaultne je rozpocet nastaven na 0).
Pod textem na rozpocet je samotny listview ktery zobrazuje vydaje zadane uzivatelem (defaultne je prazdny protoze uzivatel jeste zadne vydaje nezadal).
V dolni casti aktivity jsou 2 tlacitka a to profile a tlacitko na pridani vydaju. Obe tlacitka spusti novou aktivita a prepnou na novy layout.

![image](https://github.com/Dam0k/CapyWallet/assets/77647982/d2f396d8-72e5-489f-8d72-d316270d8082)

Zde je aktivita na pridani vydaju.
Vydaje maji nazev, castku a kategorie kterou si uzivatel zvoli pomoci spinneru tlacitkem.
Tlacitkem add uzivatel prida vydaj a ten se ulozi do databaze a zobrazi v listview na hlavnim layoutu

![image](https://github.com/Dam0k/CapyWallet/assets/77647982/17a7e19b-8c39-4f23-975f-9867bd6dd289)

Takto nejak vypada hlavni layout po pridani nejakych vydaju a bez nastaveneho rozpoctu.

![image](https://github.com/Dam0k/CapyWallet/assets/77647982/86a53fa4-2049-473a-b0a3-c3ab7ca25284)

Toto je aktivita profile ktera slouzi na pridani profiloveho obrazku pomoci kamery (defaultne je nastaven obrazek kapybary).
Pote tu je pole na zadani mesicniho prijmu ktery se pak prida do rozpoctu pomoci tlacitka save income.

![image](https://github.com/Dam0k/CapyWallet/assets/77647982/161c6a89-01aa-4080-b9d8-0b5b4e2377b5)

Takto nejak vypada hlavni layout po nastaveni prijmu a pridani nejakych vydaju.
Vydaje jdou take smazat jednoduse pomoci kliknuti na nejaky vydaj.
Vsechny data zadane uzivatelem jsou ulozene do databaze.
Vsechny vstupy jsou osetreny aby uzivatel nemohu zadat spatne nebo zadne hodnoty.
Aplikace podporuje cestinu a anglictinu.

_________________________________________________________________________________
**Technicka ukazka:**

**MainActivity**

Deklarace proměnných: Definuje proměnné pro ovládací prvky rozhraní (ListView, Button, TextView), správce databáze (DatabaseHelper) a další pomocné proměnné.

Metoda onCreate(): Tato metoda se volá při vytváření aktivity. Zde se inicializuje uživatelské rozhraní (nastavení obsahu aktivity na layout z XML souboru), nastavuje toolbar a floating action button. Dále se volá metoda displayExpenses() pro zobrazení seznamu výdajů a nastavuje se posluchač pro tlačítko profilu.

Metoda onResume(): Tato metoda se volá při obnovení aktivity. Zde se aktualizuje zobrazovaný rozpočet a seznam výdajů.

Metoda onActivityResult(): Tato metoda se volá po návratu z vedlejší aktivity (v tomto případě z aktivity profilu). Zde se aktualizuje rozpočet a zobrazí se upozornění o úspěšném uložení příjmu.

Metoda displayExpenses(): Tato metoda načte všechny výdaje z databáze a zobrazí je v seznamu ListView. Také nastavuje posluchač na kliknutí pro možnost odstranění výdaje.

Metoda deleteExpense(): Slouží k odstranění vybraného výdaje. Nejprve se ověří platnost pozice v seznamu a pak se výdaj odstraní z databáze. Následně se aktualizuje seznam výdajů a rozpočet.

Metoda updateListView(): Aktualizuje zobrazený seznam výdajů po provedení změn.

Metoda updateBudgetAmount(): Aktualizuje zobrazený rozpočet na základě uloženého příjmu a celkových výdajů. Nastavuje také barvu textu podle zbývajícího rozpočtu.

**DatabaseHelper.java**

Definice konstant a proměnných: Obsahuje definice názvů databáze, tabulek a sloupců, jakož i verze databáze.

Metody onCreate a onUpgrade: Tyto metody jsou volány při vytváření nebo aktualizaci databáze. onCreate vytváří tabulky, zatímco onUpgrade odstraňuje staré tabulky a vytváří nové.

Metody pro manipulaci s daty: Zahrnují metody pro získání příjmů, ukládání příjmů, přidání výdajů, získání všech výdajů, smazání výdajů, ukládání obrázků a získání obrázků.

SQL dotazy: SQL dotazy jsou použity k interakci s databází, jako je vytváření tabulek, vkládání, aktualizace, mazání a dotazy na data.

Manipulace s kurzorem: Kurzor je použit k přístupu k výsledkům dotazu a iteraci přes ně v metodě getAllExpenses.

Zpracování výsledků dotazů: Výsledky dotazů jsou zpracovány a použity k inicializaci objektů třídy Expense nebo pro získání konkrétních hodnot.

Správa spojení s databází: Metody otevírají a zavírají spojení s databází, aby se zabránilo úniku paměti a zbytečné spotřebě zdrojů.

**AddExpense**

Deklarace Třídy: Třída AddExpense rozšiřuje AppCompatActivity, což naznačuje, že je to aktivita v Android aplikaci.

Metoda onCreate(): Tato metoda je volána při prvním vytvoření aktivity. Inicializuje rozložení aktivity z XML souboru add_expense.xml, nastaví toolbar, naplní spinner (rozbalovací menu) kategoriemi a nastaví onClickListener pro tlačítko "Přidat".

Metoda saveExpense(): Tato metoda je volána po kliknutí na tlačítko "Přidat". Získá jméno, částku a kategorii výdaje z příslušných widgetů EditText a Spinner. Provádí ověřovací kontroly, aby se zajistilo, že obě pole jsou vyplněna, a že částka je platné číslo. Poté vytvoří objekt Expense s poskytnutými informacemi a přidá jej do databáze pomocí třídy DatabaseHelper. Zobrazí toast zprávu, která oznamuje, zda byl výdaj úspěšně přidán nebo ne.

Metoda onSupportNavigateUp(): Tato metoda je volána, když uživatel stiskne tlačítko Zpět v toolbaru. Jednoduše volá onBackPressed() pro navigaci zpět.

**Profile**

Proměnné: Jsou deklarovány různé proměnné, včetně zobrazení obrázků, tlačítek, pomocníka pro práci s databází a identifikátoru obrázku. Tyto proměnné slouží k interakci s prvkami uživatelského rozhraní a databází aplikace.

Metoda onCreate(): Tato metoda je volána při vytváření aktivity. Inicializuje rozložení, nastavuje toolbar, načítá poslední uložené ID obrázku z databáze a načte obrázek, pokud je k dispozici. Také nastavuje posluchače kliknutí pro tlačítka.

Oprávnění: Metody jako checkCameraPermission() a requestCameraPermission() zajišťují kontrolu a žádost o oprávnění k používání fotoaparátu.

Zpracování výsledků oprávnění: Metoda onRequestPermissionsResult() zpracovává výsledek žádosti o oprávnění, umožňující aplikaci pokračovat v pořizování snímku, pokud je oprávnění uděleno.

Pořizování snímku: Metoda dispatchTakePictureIntent() vytváří intent pro pořízení snímku pomocí fotoaparátu zařízení.

Zpracování pořízeného obrázku: Metoda onActivityResult() je volána, když intent fotoaparátu vrátí pořízený obrázek. Extrahuje data obrázku, zobrazí je v profilovém zobrazení obrázku a uloží je do databáze.

Ukládání obrázku do databáze: Metoda saveImageToDatabase() převede pořízený obrázek na pole bytů, uloží je do databáze a zobrazí zprávu typu toast oznamující úspěch nebo neúspěch.

Ukládání příjmů do databáze: Metoda saveIncomeToDatabase() získá uživatelův vstup s příjmem, uloží ho do databáze a zobrazí zprávu typu toast oznamující úspěch nebo neúspěch.

Navigace: Metoda onSupportNavigateUp() zajišťuje navigaci, když uživatel stiskne tlačítko zpět v toolbaru.

______________________________________________________________________________________________
**Zaver**

Tento projekt me velmi bavil a jeho vysledek se mi dokonce i celkem libi.
Nejvetsi problem byla asi databaze a porizovani obrazku jinak zbytek byl v klidu.



