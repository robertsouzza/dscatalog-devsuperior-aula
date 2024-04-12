import ProductCrudCard from "pages/Admin/Products/ProductCrudCard";
import './styles.css';
import { Link } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { SpringPage } from "types/vendor/spring";
import { Product } from "types/product";
import { AxiosRequestConfig } from "axios";
import { requestBackend } from "util/requests";
import Pagination from "components/Pagination";
import ProductFilter, { ProductFilterData } from "components/ProductFilter";

type ControlComponentsData = {
    activePage: number;
    filterData: ProductFilterData;
}

const List = () => {

    const [page, setPage] = useState<SpringPage<Product>>();

    const [controlComponentsData, setControlComponentsData] = useState<ControlComponentsData>(
     {
       activePage: 0,
       filterData: {name:"", category: null},
     })

    const handlePageChange = (pageNumber: number) =>{
        setControlComponentsData({activePage: pageNumber, filterData: controlComponentsData.filterData});
     };

    const handleSubmitfilter =(data: ProductFilterData) =>{
        setControlComponentsData({activePage: 0, filterData: data});
    } 

    const getProducts = useCallback(() => {
        const config: AxiosRequestConfig = {
            method: 'GET',
            url: "/products",
            params: {
                page: controlComponentsData.activePage,
                size: 3,
                name: controlComponentsData.filterData.name,
                categoryId: controlComponentsData.filterData.category?.id
            },
        };

        requestBackend(config)
            .then((response) => {
                setPage(response.data);
            });
    }, [controlComponentsData]);
    
    useEffect(() => {
      getProducts();

    }, [getProducts]);



    //variável temporaria
    /*
    const product ={
     "id": 1,
     "name": "The Lord of the Rings",
     "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
     "price": 90.5,
     "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg",
     "date": "2020-07-13T20:50:07.123450Z",
     "categories": [
         {
             "id": 2,
             "name": "Eletrônicos",
         },
         {
             "id": 1,
             "name": "Computadores",
         },
         {
             "id": 3,
             "name": "Celulares",
         },
     ]
    }
    */

    /*      listava rhard core na mão só para testes 
            <div className="col-sm-6 col-md-12">
                <ProductCrudCard product={product} /> 
            </div>
            <div className="col-sm-6 col-md-12">
                <ProductCrudCard product={product} /> 
            </div>
            <div className="col-sm-6 col-md-12">
                <ProductCrudCard product={product} /> 
            </div>
            <div className="col-sm-6 col-md-12">
                <ProductCrudCard product={product} /> 
            </div>

            <div className="base-card product-filter-container">Search bar</div>----
     */

    return (
        <div className="product-crud-container">
            <div className="product-crud-bar-container">
                <Link to="/admin/products/create">
                    <button className="btn btn-primary text-white btn-crud-add">
                        ADICIONAR
                    </button>
                </Link>
                <ProductFilter onSubmitFilter={handleSubmitfilter}/>
            </div>
            <div className="row">

                {page?.content.map(product => (
                    <div key={product.id} className="col-sm-6 col-md-12">
                        <ProductCrudCard product={product}
                            onDelete={() => {}} />
                    </div>
                ))}
            </div>
            <Pagination 
               forcePage={page?.number}
               pageCount={(page)? page.totalPages : 0} 
               range={3}
               onChange={handlePageChange}
            />
        </div>
    );
};

export default List;