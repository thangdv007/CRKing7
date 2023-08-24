import React from 'react';
import { useSelector } from 'react-redux';
import Api from '~/api/apis';
import { REQUEST_API } from '~/constants/method';
import { toast } from 'react-toastify';
import { RootState } from '~/redux/reducers';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Images from '~/assets';
import { Category } from '~/types/category.type';
import { useLocation, useNavigate } from 'react-router-dom';
import { Product } from '~/types/product.type';
import { API_URL_IMAGE, formatPrice } from '~/constants/utils';

const EditProduct = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const user = useSelector((state: RootState) => state.ReducerAuth.user);
  const location = useLocation();
  const productId = location.state;
  const [product, setProduct] = React.useState<Product>();
  const [productName, setProductName] = React.useState('');
  const [material, setMaterial] = React.useState('');
  const [price, setPrice] = React.useState('');
  const [description, setDescription] = React.useState('');
  const [category, setCategory] = React.useState<Category[]>([]);
  const [selectedImages, setSelectedImages] = React.useState<File[]>([]);
  const [categoryId, setCategoryId] = React.useState();
  const [addVariants, setAddVariants] = React.useState([{ color: '', size: '', quantity: '' }]);
  const [color, setColor] = React.useState('');
  const [size, setSize] = React.useState('');
  const [colorId, setColorId] = React.useState();
  const [imageId, setImageId] = React.useState([]);
  const [sizeId, setSizeId] = React.useState();
  const [quantity, setQuantity] = React.useState<number>(1);
  React.useEffect(() => {
    setProductName(product?.name || '');
    setMaterial(product?.material || '');
    setPrice(product?.price || '');
    setDescription(product?.description || '');
    setCategoryId(product?.category || '');
    // Kiểm tra xem có dữ liệu hình ảnh đã chọn từ product hay không
    if (product?.images && product.images.length > 0) {
      setSelectedImages(product.images.map((image) => new File([image], image.url)));
      setImageId(product.images.map((image) => image.id));
    } else {
      setSelectedImages([]); // Nếu không có, đặt selectedImages là một mảng rỗng
    }
    const newAddVariants = [];
    if (product?.colors) {
      // Lặp qua từng màu
      for (const color of Object.values(product.colors)) {
        setColorId(color.id);
        setColor(color.value);
        if (color.sizes) {
          // Lặp qua từng kích thước của màu đó
          for (const size of Object.values(color.sizes)) {
            setSizeId(size.id);
            setSize(size.value);
            setQuantity(size.total);
            // Tạo biến thể từ thông tin màu, kích thước và số lượng
            const variant = {
              color: color.value,
              size: size.value,
              quantity: size.total,
            };
            newAddVariants.push(variant);
          }
        }
      }
    }
    setAddVariants(newAddVariants);
  }, [product]);
  const handleColorChange = (event: React.ChangeEvent<HTMLInputElement>, index) => {
    setColor(event.target.value);
    const newVariants = [...addVariants];
    newVariants[index].color = event.target.value;
    setAddVariants(newVariants);
  };

  const handleSizeChange = (event: React.ChangeEvent<HTMLInputElement>, index) => {
    setSize(event.target.value);
    const newVariants = [...addVariants];
    newVariants[index].size = event.target.value;
    setAddVariants(newVariants);
  };
  const handleQuantityChange = (event: React.ChangeEvent<HTMLInputElement>, index) => {
    const newQuantity = parseInt(event.target.value, 10);
    setQuantity(newQuantity);
    const newVariants = [...addVariants];
    newVariants[index].quantity = event.target.value;
    setAddVariants(newVariants);
  };
  const handleaddVariants = (index) => {
    const newSection = {
      color: '',
      size: '',
      quantity: '',
    };
    setAddVariants((prevVariants) => {
      const newVariants = [...prevVariants];
      newVariants.splice(index + 1, 0, newSection);
      return newVariants;
    });
  };
  const handleRemoveVariant = (index) => {
    if (addVariants.length > 1) {
      setAddVariants((prevVariants) => {
        const newVariants = [...prevVariants];
        newVariants.splice(index, 1);
        return newVariants;
      });
    } else {
      setAddVariants((prevVariants) => {
        const newVariants = [...prevVariants];
        newVariants[index].color = '';
        newVariants[index].size = '';
        newVariants[index].quantity = '';
        return newVariants;
      });
    }
  };
  const refInputImage = React.useRef<HTMLInputElement>(null);
  const handleUpload = () => {
    refInputImage.current?.click();
  };
  const onFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const filesFromLocal = event.target.files;
    if (filesFromLocal) {
      const newSelectedImages = [...selectedImages];
      for (let i = 0; i < filesFromLocal.length; i++) {
        if (newSelectedImages.length < 4) {
          newSelectedImages.push(filesFromLocal[i]);
        } else {
          break;
        }
      }
      setSelectedImages(newSelectedImages);
    }
  };
  const handleRemoveImage = (index: number) => {
    const newSelectedImages = [...selectedImages];
    newSelectedImages.splice(index, 1);
    setSelectedImages(newSelectedImages);
  };
  const getAllCategory = async () => {
    if (!!token) {
      try {
        const url = Api.getAllCategory2();
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setCategory(res.data.data);
        } else {
          toast.error(`Có lỗi xảy ra`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  React.useEffect(() => {
    getAllCategory();
  }, []);

  const colorsData = addVariants.map((item) => ({
    id: colorId,
    value: item.color,
    sizes: [
      {
        id: sizeId,
        value: item.size,
        total: parseInt(item.quantity, 10),
      },
    ],
  }));
  const imagesData = selectedImages.map((item, i) => ({
    id: imageId[i],
    url: item.name,
  }));
  const handleUpdateProduct = async () => {
    if (!!token) {
      if (!productName) {
        toast.error(`Vui lòng nhập tên sản phẩm`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!description) {
        toast.error(`Vui lòng mô tả sản phẩm`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!material) {
        toast.error(`Vui lòng nhập chất liệu sản phẩm`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!price) {
        toast.error(`Vui lòng nhập giá sản phẩm`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (selectedImages.length < 4) {
        toast.error(`Vui lòng chọn 4 ảnh cho sản phẩm`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!categoryId) {
        toast.error(`Vui lòng chọn 1 danh mục`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!color) {
        toast.error(`Vui lòng nhập màu sắc`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!size) {
        toast.error(`Vui lòng nhập size`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (!quantity) {
        toast.error(`Vui lòng nhập số lượng`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (isNaN(Number(quantity))) {
        toast.error(`Số lượng không hợp lệ`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      if (isNaN(Number(price))) {
        toast.error(`Giá không hợp lệ`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        return;
      }
      const data = {
        id: productId,
        name: productName,
        description: description,
        material: material,
        price: price,
        categoryId: categoryId,
        userId: user.id,
        colors: colorsData,
        images: imagesData,
      };
      try {
        const url = Api.updateProduct();
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'put',
            token: token,
            data: data,
          }),
        ]);
        console.log(res);

        if (res.status) {
          getProduct();
          toast.success(`Cập nhật sản phẩm thành công`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        } else {
          toast.error(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const getProduct = async () => {
    if (!!token) {
      try {
        const url = Api.detailProduct(productId);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setProduct(res.data);
        } else {
          toast.error(`Có lỗi xảy ra`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  React.useEffect(() => {
    getProduct();
  }, []);
  const handleCategoryChange = (e) => {
    const selectedCategory = e.target.value;
    if (selectedCategory !== '--Chọn danh mục--') {
      setCategoryId(selectedCategory);
    }
  };

  return (
    <>
      {/* Phần 1 */}
      <div className="flex items-center justify-around mt-0">
        {/* Bên phải */}
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold text-blue">Thông tin sản phẩm</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <span className="text-base text-black font-bold">Tên sản phẩm: </span>
            <input
              className="h-9 ml-5 pl-2 border-black border-[1px] rounded-lg w-[70%]"
              placeholder="Nhập tên sản phẩm"
              value={productName}
              onChange={(e) => setProductName(e.target.value)}
            />
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base text-black font-bold">Chất liệu: </span>
            <input
              className="h-9 ml-5 pl-2 border-black border-[1px] rounded-lg w-[70%]"
              placeholder="Nhập chất liệu"
              value={material}
              onChange={(e) => setMaterial(e.target.value)}
            />
          </div>
          <div className="flex flex-col pt-5">
            <span className="text-base text-black font-bold">Mô tả: </span>
            <ReactQuill
              theme="snow"
              value={description}
              onChange={setDescription}
              className="w-full"
              placeholder="Nhập mô tả ở đây"
            />
            {/* <Editor placeholder={'Viết Mô tả ở đây'}/> */}
          </div>
        </div>
        {/* Bên trái */}
        <div className="w-[25%] flex flex-col border rounded-md p-5 self-start">
          <span className="text-lg font-semibold text-blue">Giá</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <span className="text-base text-black font-bold">Giá: </span>
            <input
              className="h-9 ml-5 pl-2 border-black border-[1px] rounded-lg w-[70%]"
              placeholder="Nhập giá"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
            />
          </div>
        </div>
      </div>
      {/* Phần 2 */}
      <div className="flex items-center justify-around mt-5">
        {/* Bên phải */}
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold text-blue">Hình ảnh</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="w-full h-[170px] border-black border border-dashed rounded-lg mt-5">
            <input
              className="hidden"
              type="file"
              accept=".jpg,.jpeg,.png"
              multiple
              ref={refInputImage}
              onChange={onFileChange}
            />
            <div className="flex items-center justify-around cursor-pointer">
              {selectedImages.map((image, index) => (
                <div key={index} className="relative">
                  <img src={`${API_URL_IMAGE}${image.name}`} className="w-40 h-40 object-contain pt-2" />
                  <div
                    onClick={() => handleRemoveImage(index)}
                    className="absolute w-[20px] h-[20px] rounded items-center justify-center flex bg-[#00000080] top-[5%] right-[0]"
                  >
                    <img src={Images.iconX} className="w-[10px] h-[10px]" />
                  </div>
                </div>
              ))}
              {selectedImages.length < 4 && (
                <div onClick={handleUpload} className="flex flex-col items-center justify-center">
                  <img src={Images.chooseImage} className="w-[120px] h-[120px] object-contain pt-2" />
                  <span>Chọn ảnh để tải lên</span>
                </div>
              )}
            </div>
          </div>
          <div className="flex items-center justify-center mt-5">
            <span className="text-sm text-black">Hình ảnh đầu tiên và thứ 2 được làm ảnh đại diện</span>
          </div>
        </div>
        {/* Bên trái */}
        <div className="w-[25%] flex flex-col border rounded-md p-5 self-start">
          <span className="text-lg font-semibold text-blue">Danh mục</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <div className="w-full">
              <select
                name=""
                id=""
                className="h-9 border-black text-black border-[1px] rounded-lg appearance-none text-center w-[80%]"
                onChange={handleCategoryChange}
                value={categoryId}
              >
                <option value="">--Chọn danh mục--</option>
                {!!category &&
                  !!category.length &&
                  category.map((item, i) => (
                    <option value={item.id} key={i} className="text-black">
                      {item.title}
                    </option>
                  ))}
              </select>
            </div>
          </div>
        </div>
      </div>
      {/* Phần 3 */}
      <div className="flex items-center justify-around mt-5">
        {/* Bên phải */}
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold text-blue">Chi tiết</span>
          <div className="w-full h-[1px] bg-black"></div>
          {!!addVariants &&
            !!addVariants.length &&
            addVariants.map((item, i) => (
              <div className="flex mt-3" key={i}>
                <div className="">
                  <label htmlFor={`color-${i}`} className="block font-medium mb-1 cursor-pointer">
                    Màu sắc:
                  </label>
                  <input
                    type="text"
                    id={`color-${i}`}
                    value={item.color}
                    onChange={(e) => handleColorChange(e, i)}
                    className="p-2 border border-black rounded-md w-40"
                  />
                </div>
                {color && (
                  <div className="ml-4">
                    <label htmlFor={`size-${i}`} className="block font-medium mb-1 cursor-pointer">
                      Kích thước:
                    </label>
                    <input
                      type="text"
                      id={`size-${i}`}
                      value={item.size}
                      onChange={(e) => handleSizeChange(e, i)}
                      className="p-2 border border-black rounded-md w-40"
                    />
                  </div>
                )}
                {size && color && (
                  <div className="ml-4">
                    <label htmlFor={`quantity-${i}`} className="block font-medium mb-1 cursor-pointer">
                      Số lượng:
                    </label>
                    <input
                      id={`quantity-${i}`}
                      value={item.quantity}
                      onChange={(e) => handleQuantityChange(e, i)}
                      className="p-2 border border-black rounded-md w-16"
                      inputMode="numeric" // Đặt kiểu nhập liệu là số
                      pattern="[0-9]*"
                    />
                  </div>
                )}
                {size && color && quantity && (
                  <div className="flex items-end justify-center ml-4">
                    <div
                      className="bg-blue cursor-pointer text-white px-4 rounded-md flex items-center justify-center"
                      onClick={() => handleaddVariants(i)}
                    >
                      <span>Thêm</span>
                    </div>
                    <div
                      className="cursor-pointer ml-4 bg-red-500 text-white px-4 rounded-md flex items-center justify-center"
                      onClick={() => handleRemoveVariant(i)}
                    >
                      <span>Xóa</span>
                    </div>
                  </div>
                )}
              </div>
            ))}
          <div className=""></div>
        </div>
        {/* Bên trái */}
        <div className="w-[25%] self-end flex justify-center">
          <div
            className="bg-blue h-10 flex items-center justify-center rounded-lg cursor-pointer w-[50%]"
            onClick={() => handleUpdateProduct()}
          >
            <span className="text-white">Lưu</span>
          </div>
        </div>
      </div>
    </>
  );
};

export default EditProduct;
