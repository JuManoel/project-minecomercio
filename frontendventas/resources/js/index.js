class App{
    static async main() {
        let response=await fetch("http://localhost:7070/producto")
        let r=await response.json()
        document.querySelector("#titulo").innerHTML=r.data[0]
        let data=r.data
        console.log(data[0])
    }
}

App.main()