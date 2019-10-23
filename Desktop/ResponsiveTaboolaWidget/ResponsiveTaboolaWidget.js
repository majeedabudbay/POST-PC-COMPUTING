const api = 'https://api.taboola.com/1.2/json/apitestaccount/recommendations.get?app.type=web&app.apikey=7be65fc78e52c11727793f68b06d782cff9ede3c&source.id=%2Fdigiday-publishing-summit%2F&source.url=https%3A%2F%2Fblog.taboola.com%2Fdigiday-publishing-summit%2F&source.type=text&placement.organic-type=mix&placement.visible=true&placement.available=true&placement.rec-count=6&placement.name=Below%20Article%20Thumbnails&placement.thumbnail.width=640&placement.thumbnail.height=480&user.session=init';
const widget = document.querySelector('#widget');



function getTag(tag) {
  return document.querySelector(tag);
}

function createHtmlElem(tag) {
  return document.createElement(tag);
}

function appendingToParent(pTag, cTag) {
  return pTag.appendChild(cTag);
}

function addUrl(tag, url) {
  return tag.href = url;
}

function truncate(text){
   if (text.length > 40){
    return text.substring(0,40)+'...';
  }else{
      return text;
    }
}


fetch(api)
  .then(function(response) {
    return response.json();
  })
  .then(function(data) {
    showWidget(data.list);
  })
  .catch(function(err) {
    console.log("Error!", err);
  });



let header = getTag('header');
let h6 = createHtmlElem('h6');
h6.innerHTML = `<span>Taboola</span>`;
h6.className = 'pg-title';
appendingToParent(header, h6);

function showWidget(items) {
  let data = items;

  return data.map((item) => {
    let container = createHtmlElem('div');

    let img = createHtmlElem('a')
    addUrl(img, item.url)
    img.innerHTML = `<img src="${item.thumbnail[0].url}"/>`
    appendingToParent(container, img);

    let branding = createHtmlElem('a')
    addUrl(branding, item.url)
    branding.innerHTML = `<h5 class="branding">${item.branding}</h5>`
    appendingToParent(container, branding);

    if (item.categories) {
      let categories = createHtmlElem('p')
      categories.className += "categories"
      categories.innerHTML = `${item.categories.join()}`
      appendingToParent(container, categories);
    }

    let title = createHtmlElem('a')
    addUrl(title, item.url)
    title.innerHTML = `<h3 class="title">${item.name}</h3>`
    appendingToParent(container, title);

    appendingToParent(widget, container);

  });
}



